package org.jsweet.transpiler.eval;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.jsweet.transpiler.util.EvaluationResult;
import org.jsweet.transpiler.util.ProcessUtil;

public class JavaScriptEval extends RuntimeEval {

    private static final String BABEL_EXECUTABLE_NAME = "babel";
    private static final String BABEL_PACKAGE_NAME = "@babel/cli";
    private static final String BABEL_CORE_PACKAGE_NAME = "@babel/core";
    private static final String BABEL_PRESET_PACKAGE_NAME = "@babel/preset-env";

    private static final String PHANTOMJS_EXECUTABLE_NAME = "phantomjs";
    private static final String PHANTOMJS_PACKAGE_NAME = "phantomjs-prebuilt";

    public static enum JavaScriptRuntime {
        NodeJs, PhantomJs;
    }

    private EvalOptions options;
    private JavaScriptRuntime runtime;

    public JavaScriptEval(EvalOptions options, JavaScriptRuntime runtime) {
        this.options = options;
        this.runtime = runtime;
    }

    public EvaluationResult performEval(Collection<File> jsFiles) throws Exception {

        StringWriter trace = new StringWriter();

        Process runProcess;
        if (options.useModules) {
            logger.info("[modules] eval file: " + jsFiles);
            if (jsFiles.size() == 0) {
                logger.warn("no files were transpiled");
                return EvaluationResult.VOID;
            }

            File mainFile = jsFiles.iterator().next();

            runProcess = runScript(trace, mainFile);
        } else {

            File tmpFile = new File(options.workingDir, "eval.tmp_" + System.currentTimeMillis() + ".js");
            FileUtils.deleteQuietly(tmpFile);

            File j4tsJsPath = options.useJavaRuntime;
            if (j4tsJsPath != null) {
                List<File> newFiles = new ArrayList<>(jsFiles);
                newFiles.add(0, j4tsJsPath);
                jsFiles = newFiles;
            }

            Set<File> alreadyWrittenScripts = new HashSet<>();
            for (File jsFile : jsFiles) {
                if (!alreadyWrittenScripts.contains(jsFile)) {
                    String script = FileUtils.readFileToString(jsFile);
                    FileUtils.write(tmpFile, script + "\n", true);
                    alreadyWrittenScripts.add(jsFile);
                }
            }

            logger.info("[no modules] eval file: " + tmpFile + " jsFiles=" + jsFiles + ", useJavaRuntime=" + options.useJavaRuntime);

            runProcess = runScript(trace, tmpFile);
        }

        int returnCode = runProcess.exitValue();
        logger.info("return code=" + returnCode);
        if (returnCode != 0) {
            throw new Exception("evaluation error (code=" + returnCode + ") - trace=" + trace);
        }
        return new TraceBasedEvaluationResult(trace.getBuffer().toString());
    }

    private Process runScript(StringWriter trace, File mainFile) throws IOException {
        Process runProcess;
        if (runtime == JavaScriptRuntime.PhantomJs) {

            File projectDir = mainFile.getParentFile();
            checkBabelInstall(projectDir);
            checkPhantomJsInstall();

            String phantomJsPath = resolvePhantomJsPath();
            String babelPath = resolveBabelPath();

            // RUN BABEL
            runProcess = ProcessUtil.runCommand( //
                    babelPath, //
                    null, //
                    false, //
                    line -> {
                        logger.info("BABEL OUT: " + line);
                        trace.append(line + "\n");
                    }, process -> {
                        logger.info("BABEL END - " + process);
                        trace.append("babel ended\n");
                    }, () -> {
                        trace.append("BABEL errored\n");
                        logger.info("babel ERROR :(");
                    }, //
                    mainFile.getPath(), //
                    "--out-file=" + mainFile.getPath(), //
                    "--presets=" + BABEL_PRESET_PACKAGE_NAME);

            // RUN PHANTOMJS
            runProcess = ProcessUtil.runCommand( //
                    phantomJsPath, //
                    null, //
                    false, //
                    line -> {
                        logger.info("PHANTOM OUT: " + line);
                        if (line.startsWith("TEST_DATA_ERROR")) {
                            String errorMessage = line.split("[:]")[1];
                            throw new RuntimeException("error during execution: " + errorMessage);
                        }
                        trace.append(line + "\n");
                    }, process -> {
                        logger.info("PHANTOM END - " + process);
                        trace.append("phantom ended\n");
                    }, () -> {
                        trace.append("phantom errored\n");
                        logger.info("phantomERROR :(");
                    }, //
                       // debugger causes hanging process, never returning..
                       // "--remote-debugger-port=9000", //
                       // "--remote-debugger-autorun=true", //
                       // "--debug=true", //
                    "--webdriver-loglevel=DEBUG", //
                    mainFile.getPath());

        } else {
            runProcess = ProcessUtil.runCommand(ProcessUtil.NODE_COMMAND, line -> trace.append(line + "\n"), null,
                    mainFile.getPath());
        }

        return runProcess;
    }

    private String resolvePhantomJsPath() {
        String phantomJsPath = ProcessUtil.getGlobalNpmPackageExecutablePath(PHANTOMJS_EXECUTABLE_NAME);
        if (ProcessUtil.isWindows()) {
            // we need to use the .exe file to be able to fix this bug:
            // https://github.com/ariya/phantomjs/issues/10845#issuecomment-24220355
            phantomJsPath = ProcessUtil.findGlobalExecutable("phantomjs.exe", PHANTOMJS_PACKAGE_NAME);
            if (phantomJsPath == null || !new File(phantomJsPath).isFile()) {
                phantomJsPath = ProcessUtil.getGlobalNpmPackageExecutablePath(PHANTOMJS_EXECUTABLE_NAME);
            }
        }
        logger.info("phantomJsPath=" + phantomJsPath);
        if (!new File(phantomJsPath).canExecute()) {
            throw new RuntimeException("phantomjs cannot be found");
        }

        return phantomJsPath;
    }

    private String resolveBabelPath() {
        String babelPath = ProcessUtil.getGlobalNpmPackageExecutablePath(BABEL_EXECUTABLE_NAME);
        logger.info("babelPath=" + babelPath);
        if (!new File(babelPath).canExecute()) {
            throw new RuntimeException("babel cannot be found");
        }

        return babelPath;
    }

    private void checkPhantomJsInstall() {
        if (!ProcessUtil.isExecutableInstalledGloballyWithNpm(PHANTOMJS_EXECUTABLE_NAME)) {
            ProcessUtil.installGlobalNodePackage(PHANTOMJS_PACKAGE_NAME, null);
        }
    }

    private void checkBabelInstall(File projectDirectory) {
        if (!ProcessUtil.isPackageInstalledLocallyWithNpm(BABEL_EXECUTABLE_NAME, projectDirectory)) {
            ProcessUtil.installLocalNodePackage(BABEL_PACKAGE_NAME, null, projectDirectory);
        }
        if (!ProcessUtil.isPackageInstalledLocallyWithNpm(BABEL_CORE_PACKAGE_NAME, projectDirectory)) {
            ProcessUtil.installLocalNodePackage(BABEL_CORE_PACKAGE_NAME, null, projectDirectory);
        }
        if (!ProcessUtil.isPackageInstalledLocallyWithNpm(BABEL_PRESET_PACKAGE_NAME, projectDirectory)) {
            ProcessUtil.installLocalNodePackage(BABEL_PRESET_PACKAGE_NAME, null, projectDirectory);
        }
    }
}
