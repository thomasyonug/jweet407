/* 
 * JSweet transpiler - http://www.jsweet.org
 * Copyright (C) 2015 CINCHEO SAS <renaud.pawlak@cincheo.fr>
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *  
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package org.jsweet.transpiler;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.jsweet.transpiler.util.ProcessUtil.isVersionHighEnough;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UncheckedIOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.JavaVersion;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.jsweet.JSweetConfig;
import org.jsweet.transpiler.candy.CandyProcessor;
import org.jsweet.transpiler.eval.EvalOptions;
import org.jsweet.transpiler.eval.JavaEval;
import org.jsweet.transpiler.eval.JavaScriptEval;
import org.jsweet.transpiler.eval.JavaScriptEval.JavaScriptRuntime;
import org.jsweet.transpiler.extension.ExtensionManager;
import org.jsweet.transpiler.extension.PrinterAdapter;
import org.jsweet.transpiler.util.AbstractTreePrinter;
import org.jsweet.transpiler.util.DirectedGraph;
import org.jsweet.transpiler.util.DirectedGraph.Node;
import org.jsweet.transpiler.util.ErrorCountTranspilationHandler;
import org.jsweet.transpiler.util.EvaluationResult;
import org.jsweet.transpiler.util.Position;
import org.jsweet.transpiler.util.ProcessUtil;
import org.jsweet.transpiler.util.SourceMap;
import org.jsweet.transpiler.util.SourceMap.Entry;
import org.jsweet.transpiler.util.Util;

import com.google.debugging.sourcemap.FilePosition;
import com.google.debugging.sourcemap.OriginalMapping;
import com.google.debugging.sourcemap.SourceMapConsumerFactory;
import com.google.debugging.sourcemap.SourceMapFormat;
import com.google.debugging.sourcemap.SourceMapGenerator;
import com.google.debugging.sourcemap.SourceMapGeneratorFactory;
import com.google.debugging.sourcemap.SourceMapGeneratorV3;
import com.google.debugging.sourcemap.SourceMapping;
import com.google.gson.Gson;
import standalone.com.sun.source.tree.CompilationUnitTree;
import standalone.com.sun.source.tree.Tree;

/**
 * The actual JSweet transpiler.
 * 
 * <p>
 * Instantiate this class to transpile Java to TypeScript (phase 1), and
 * TypeScript to JavaScript (phase 2).
 * 
 * <p>
 * There are 2 phases in JSweet transpilation:
 *
 * <ol>
 * <li>Java to TypeScript</li>
 * <li>TypeScript to JavaScript</li>
 * </ol>
 *
 * <p>
 * In phase 1, JSweet delegates to Javac and applies the
 * {@link Java2TypeScriptTranslator} AST visitor to print out the TypeScript
 * code. Before printing out the code, the transpiler first applies AST
 * visitors: {@link GlobalBeforeTranslationScanner},
 * {@link StaticInitilializerAnalyzer}, and {@link TypeChecker}. All external
 * referenced classes must be in the classpath for this phase to succeed. Note
 * that this generation is fully customizable with the
 * {@link org.jsweet.transpiler.extension} API.
 *
 * <p>
 * In phase 2, JSweet delegates to tsc (TypeScript). TypeScript needs to have a
 * TypeScript typing definition for all external classes. Existing JSweet
 * candies (http://www.jsweet.org/jsweet-candies/) are Maven artifacts that come
 * both with the compiled Java implementation/definition in a Jar, and the
 * associated TypeScript definitions for phase 2.
 * 
 * @author Renaud Pawlak
 * @author Louis Grignon
 */
public class JSweetTranspiler implements JSweetOptions, AutoCloseable {

    /**
     * The TypeScript version to be installed/used with this version of JSweet
     * (WARNING: so far, having multiple JSweet versions for the same user account
     * may lead to performance issues - could be fixed if necessary).
     */
    public static final String TSC_VERSION = "5.2";

    static {
        if (!SystemUtils.isJavaVersionAtLeast(JavaVersion.JAVA_1_8)) {
            throw new RuntimeException("JSweet is currently only supported for JDK 8->11, please use JDK 8->11.");
        }
    }

    /**
     * Gets the target version from a user-friendly descriptive string.
     */
    public static EcmaScriptComplianceLevel getEcmaTargetVersion(String targetVersion) {
        try {
            EcmaScriptComplianceLevel ecmaScriptComplianceLevel = EcmaScriptComplianceLevel.valueOf(targetVersion);
            return ecmaScriptComplianceLevel;
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid EcmaScript target version: " + targetVersion);
        }
    }

    /**
     * A constant that is used for exporting variables.
     * 
     * @see TraceBasedEvaluationResult
     * @see #eval(TranspilationHandler, SourceFile...)
     */
    public static final String EXPORTED_VAR_BEGIN = "EXPORT ";
    /**
     * A constant that is used for exporting variables.
     * 
     * @see TraceBasedEvaluationResult
     * @see #eval(TranspilationHandler, SourceFile...)
     */
    public static final String EXPORTED_VAR_END = ";";
    public static Pattern EXPORTED_VAR_REGEXP = Pattern.compile(EXPORTED_VAR_BEGIN + "(\\w*)=(.*)" + EXPORTED_VAR_END);

    private final static Logger logger = Logger.getLogger(JSweetTranspiler.class);

    /**
     * The name of the file generated in the root package to avoid the TypeScript
     * compiler to skip empty directories.
     * 
     * @deprecated Use {@link TypeScript2JavaScriptWithTscTranspiler#TSCROOTFILE}
     *             instead
     */
    public final static String TSCROOTFILE = TypeScript2JavaScriptWithTscTranspiler.TSCROOTFILE;

    private JSweetFactory factory;
    private PrinterAdapter adapter;
    private long transpilationStartTimestamp;

    private JSweetContext context;

    private JavaCompilationComponents javaCompilationComponents;

    private CandyProcessor candiesProcessor;
    private boolean generateSourceMaps = false;
    private final ConfiguredDirectory workingDirectory;
    private final File workingDir;
    private File tsOutputDir;
    private File jsOutputDir;
    private String classPath;
    private boolean generateTsFiles = true;
    private boolean generateJsFiles = true;
    private boolean tscWatchMode = false;
    private final LinkedList<File> tsDefDirs = new LinkedList<>();
    private ModuleKind moduleKind = ModuleKind.none;
    private ModuleResolution moduleResolution = ModuleResolution.classic;
    private EcmaScriptComplianceLevel ecmaTargetVersion = EcmaScriptComplianceLevel.ES3;
    private boolean bundle = false;
    private String encoding = null;
    private String outEncoding = "UTF-8";
    private boolean noRootDirectories = false;
    private boolean ignoreAssertions = true;
    private boolean ignoreJavaFileNameError = false;
    private boolean generateDeclarations = false;
    private File declarationsOutputDir;
    private boolean generateDefinitions = true;
    private ArrayList<File> jsLibFiles = new ArrayList<>();
    private File sourceRoot = null;
    private boolean ignoreTypeScriptErrors = false;
    private boolean ignoreJavaErrors = false;
    private boolean forceJavaRuntime = false;
    private File javaRuntimeJ4TsJs = null;
    private File headerFile = null;
    private boolean debugMode = false;
    private boolean skipTypeScriptChecks = false;
    private boolean disableSingleFloatPrecision = false;
    private boolean ignoreCandiesTypeScriptDefinitions = false;
    private boolean lazyInitializedStatics = true;
    private boolean useSingleQuotesForStringLiterals = false;
    private boolean nonEnumerableTransients = false;
    private int hangingTscTimeout = 10;
    private boolean sortClassMembers = false;

    private boolean autoPropagateAsyncAwaits = false;
    private String[] javaCompilerExtraOptions = {};

    private ArrayList<String> adapters = new ArrayList<>();
    private File configurationFile;

    private TypeScript2JavaScriptTranspiler ts2jsTranspiler = new TypeScript2JavaScriptWithTscTranspiler();

    /**
     * Manually sets the transpiler to use (or not use) a Java runtime.
     * 
     * <p>
     * Calling this method is usually not needed since JSweet auto-detects the J4TS
     * candy. Use only to manually force the transpiler in a mode or another.
     */
    public void setUsingJavaRuntime(File pathToJ4TsJs) {
        forceJavaRuntime = true;
        javaRuntimeJ4TsJs = pathToJ4TsJs;
    }

    @Override
    public String toString() {
        return "workingDir=" + workingDir + "\ntsOutputDir=" + tsOutputDir + "\njsOutputDir=" + jsOutputDir
                + "\nclassPath=" + classPath + "\ngenerateJsFiles=" + generateJsFiles + "\ntscWatchMode=" + tscWatchMode
                + "\ntsDefDirs=" + tsDefDirs + "\nmoduleKind=" + moduleKind + "\necmaTargertVersion="
                + ecmaTargetVersion + "\nbundle=" + bundle + "\nencoding=" + encoding + "\nnoRootDirectories="
                + noRootDirectories + "\nignoreAssertions=" + ignoreAssertions + "\nignoreJavaFileNameError="
                + ignoreJavaFileNameError + "\ngenerateDeclarations=" + generateDeclarations
                + "\ndeclarationsOutputDir=" + declarationsOutputDir + "\ngenerateDefinitions=" + generateDefinitions
                + "\njsLibFiles=" + jsLibFiles;
    }

    /**
     * Creates a JSweet transpiler, with the default values.
     * 
     * <p>
     * TypeScript and JavaScript output directories are set to
     * <code>System.getProperty("java.io.tmpdir")</code>. The classpath is set to
     * <code>System.getProperty("java.class.path")</code>.
     * 
     * @param factory the factory used to create the transpiler objects
     * @throws IOException on error.
     */
    public JSweetTranspiler(JSweetFactory factory) throws IOException {
        this(factory, new File(System.getProperty("java.io.tmpdir")), null, null,
                System.getProperty("java.class.path"));
    }

    /**
     * Creates a JSweet transpiler.
     * 
     * @param factory                       the factory used to create the
     *                                      transpiler objects
     * @param tsOutputDir                   the directory where TypeScript files are
     *                                      written
     * @param jsOutputDir                   the directory where JavaScript files are
     *                                      written
     * @param extractedCandiesJavascriptDir see
     *                                      {@link #getExtractedCandyJavascriptDir()}
     * @param classPath                     the classpath as a string (check out
     *                                      system-specific requirements for Java
     *                                      classpathes)
     * @throws IOException on error.
     */
    public JSweetTranspiler(JSweetFactory factory, File tsOutputDir, File jsOutputDir,
            File extractedCandiesJavascriptDir, String classPath) throws IOException {
        this(factory, null, tsOutputDir, jsOutputDir, extractedCandiesJavascriptDir, classPath);
    }

    private Map<String, Object> configuration;

    private File baseDirectory;

    @SuppressWarnings("unchecked")
    private <T> T getMapValue(Map<String, Object> map, String key) {
        return (T) map.get(key);
    }

    /**
     * Applies the current configuration map.
     */
    private void applyConfiguration() {
        if (configuration.containsKey("options")) {

            @SuppressWarnings("unchecked")
            Map<String, Object> options = (Map<String, Object>) configuration.get("options");

            for (String key : options.keySet()) {
                if (!ArrayUtils.contains(JSweetOptions.options, key)) {
                    logger.error("unsupported option: " + key);
                }
            }
            if (options.containsKey(JSweetOptions.bundle)) {
                setBundle(getMapValue(options, JSweetOptions.bundle));
            }
            if (options.containsKey(JSweetOptions.noRootDirectories)) {
                setNoRootDirectories(getMapValue(options, JSweetOptions.noRootDirectories));
            }
            if (options.containsKey(JSweetOptions.sourceMap)) {
                setGenerateSourceMaps(getMapValue(options, JSweetOptions.sourceMap));
            }
            if (options.containsKey(JSweetOptions.module)) {
                setModuleKind(ModuleKind.valueOf(getMapValue(options, JSweetOptions.module)));
            }
            if (options.containsKey(JSweetOptions.encoding)) {
                setEncoding(getMapValue(options, JSweetOptions.encoding));
            }
            if (options.containsKey(JSweetOptions.outEncoding)) {
                setOutEncoding(getMapValue(options, JSweetOptions.outEncoding));
            }
            if (options.containsKey(JSweetOptions.enableAssertions)) {
                setIgnoreAssertions(!(Boolean) getMapValue(options, JSweetOptions.enableAssertions));
            }
            if (options.containsKey(JSweetOptions.declaration)) {
                setGenerateDeclarations(getMapValue(options, JSweetOptions.declaration));
            }
            if (options.containsKey(JSweetOptions.tsOnly)) {
                setGenerateJsFiles(!(Boolean) getMapValue(options, JSweetOptions.tsOnly));
            }
            if (options.containsKey(JSweetOptions.ignoreDefinitions)) {
                setGenerateDefinitions(!(Boolean) getMapValue(options, JSweetOptions.ignoreDefinitions));
            }
            if (options.containsKey(JSweetOptions.header)) {
                setHeaderFile(new File((String) getMapValue(options, JSweetOptions.header)));
            }
            if (options.containsKey(JSweetOptions.disableSinglePrecisionFloats)) {
                setDisableSinglePrecisionFloats(getMapValue(options, JSweetOptions.disableSinglePrecisionFloats));
            }
            if (options.containsKey(JSweetOptions.disableStaticsLazyInitialization)) {
                setLazyInitializedStatics(getMapValue(options, JSweetOptions.disableStaticsLazyInitialization));
            }
            if (options.containsKey(JSweetOptions.targetVersion)) {
                setEcmaTargetVersion(
                        JSweetTranspiler.getEcmaTargetVersion(getMapValue(options, JSweetOptions.targetVersion)));
            }
            if (options.containsKey(JSweetOptions.tsout)) {
                setTsOutputDir(new File((String) getMapValue(options, JSweetOptions.tsout)));
            }
            if (options.containsKey(JSweetOptions.dtsout)) {
                setDeclarationsOutputDir(new File((String) getMapValue(options, JSweetOptions.dtsout)));
            }
            if (options.containsKey(JSweetOptions.jsout)) {
                setJsOutputDir(new File((String) getMapValue(options, JSweetOptions.jsout)));
            }
            if (options.containsKey(JSweetOptions.candiesJsOut)) {
                setJsOutputDir(new File((String) getMapValue(options, JSweetOptions.candiesJsOut)));
            }
            if (options.containsKey(JSweetOptions.moduleResolution)) {
                setModuleResolution(getMapValue(options, JSweetOptions.moduleResolution));
            }
            if (options.containsKey(JSweetOptions.extraSystemPath)) {
                ProcessUtil.addExtraPath(extraSystemPath);
            }
            if (options.containsKey(JSweetOptions.useSingleQuotesForStringLiterals)) {
                setUseSingleQuotesForStringLiterals(
                        (Boolean) getMapValue(options, JSweetOptions.useSingleQuotesForStringLiterals));
            }
            if (options.containsKey(JSweetOptions.ignoreJavaErrors)) {
                setIgnoreJavaErrors((Boolean) getMapValue(options, JSweetOptions.ignoreJavaErrors));
            }
            if (options.containsKey(JSweetOptions.nonEnumerableTransients)) {
                setNonEnumerableTransients((Boolean) getMapValue(options, JSweetOptions.nonEnumerableTransients));
            }
            if (options.containsKey(JSweetOptions.hangingTscTimeout)) {
                String s=getMapValue(options, JSweetOptions.hangingTscTimeout);
                if(s!=null) {
                    setHangingTscTimeout(Integer.parseInt(s));
                }
            }
        }

    }

    /**
     * Reads configuration from current configuration file.
     */
    private void readConfiguration() {
        File confFile = configurationFile == null ? new File(baseDirectory, JSweetConfig.CONFIGURATION_FILE_NAME)
                : configurationFile;
        if (confFile.exists()) {
            try {
                logger.info("configuration file found: " + confFile);
                @SuppressWarnings("unchecked")
                Map<String, Object> fromJson = new Gson().fromJson(FileUtils.readFileToString(confFile), Map.class);
                configuration = fromJson;
                logger.debug("configuration: " + configuration);
                applyConfiguration();
            } catch (Exception e) {
                logger.warn("error reading configuration file", e);
            }
        } else {
            logger.info("no configuration file found at " + confFile.getAbsolutePath());
        }
    }

    /**
     * Creates a JSweet transpiler.
     * 
     * @param factory                       the factory used to create the
     *                                      transpiler objects
     * @param workingDir                    the working directory (uses default one
     *                                      if null)
     * @param tsOutputDir                   the directory where TypeScript files are
     *                                      written
     * @param jsOutputDir                   the directory where JavaScript files are
     *                                      written
     * @param extractedCandiesJavascriptDir see
     *                                      {@link #getExtractedCandyJavascriptDir()}
     * @param classPath                     the classpath as a string (check out
     *                                      system-specific requirements for Java
     *                                      classpaths)
     * @throws IOException on error.
     */
    public JSweetTranspiler(JSweetFactory factory, File workingDir, File tsOutputDir, File jsOutputDir,
            File extractedCandiesJavascriptDir, String classPath) throws IOException {
        this(null, factory, workingDir, tsOutputDir, tsOutputDir, extractedCandiesJavascriptDir, classPath);
    }

    /**
     * Creates a JSweet transpiler.
     * 
     * @param configurationFile             the configurationFile (uses default one
     *                                      if null)
     * @param factory                       the factory used to create the
     *                                      transpiler objects
     * @param workingDir                    the working directory (uses default one
     *                                      if null)
     * @param tsOutputDir                   the directory where TypeScript files are
     *                                      written
     * @param jsOutputDir                   the directory where JavaScript files are
     *                                      written
     * @param extractedCandiesJavascriptDir see
     *                                      {@link #getExtractedCandyJavascriptDir()}
     * @param classPath                     the classpath as a string (check out
     *                                      system-specific requirements for Java
     *                                      classpaths)
     * @throws IOException on error.
     */
    public JSweetTranspiler(File configurationFile, JSweetFactory factory, File workingDir, File tsOutputDir,
            File jsOutputDir, File extractedCandiesJavascriptDir, String classPath) throws IOException {
        this(null, configurationFile, factory, workingDir, tsOutputDir, jsOutputDir, extractedCandiesJavascriptDir,
                classPath);
    }

    /**
     * Creates a JSweet transpiler.
     * 
     * @param configurationFile             the configurationFile (uses default one
     *                                      if null)
     * @param factory                       the factory used to create the
     *                                      transpiler objects
     * @param workingDir                    the working directory (uses default one
     *                                      if null)
     * @param tsOutputDir                   the directory where TypeScript files are
     *                                      written
     * @param jsOutputDir                   the directory where JavaScript files are
     *                                      written
     * @param extractedCandiesJavascriptDir see
     *                                      {@link #getExtractedCandyJavascriptDir()}
     * @param classPath                     the classpath as a string (check out
     *                                      system-specific requirements for Java
     *                                      classpaths)
     * @throws IOException on error.
     */
    public JSweetTranspiler(File baseDirectory, File configurationFile, JSweetFactory factory, File workingDir,
            File tsOutputDir, File jsOutputDir, File extractedCandiesJavascriptDir, String classPath) throws IOException {
      this(baseDirectory, configurationFile, factory, ConfiguredDirectory.ofDirOrNull(workingDir), tsOutputDir, jsOutputDir, ConfiguredDirectory.ofDirOrNull(extractedCandiesJavascriptDir), classPath);
    }
    
    /**
     * Creates a JSweet transpiler.
     * 
     * @param configurationFile             the configurationFile (uses default one
     *                                      if null)
     * @param factory                       the factory used to create the
     *                                      transpiler objects
     * @param workingDir                    the working directory (uses default one
     *                                      if null)
     * @param tsOutputDir                   the directory where TypeScript files are
     *                                      written
     * @param jsOutputDir                   the directory where JavaScript files are
     *                                      written
     * @param extractedCandiesJavascriptDir see
     *                                      {@link #getExtractedCandyJavascriptDir()}
     * @param classPath                     the classpath as a string (check out
     *                                      system-specific requirements for Java
     *                                      classpaths)
     * @throws IOException on error.
     */
    public JSweetTranspiler(File baseDirectory, File configurationFile, JSweetFactory factory, ConfiguredDirectory workingDir,
        File tsOutputDir, File jsOutputDir, ConfiguredDirectory extractedCandiesJavascriptDir, String classPath) throws IOException {
        if (baseDirectory == null) {
            baseDirectory = new File(".");
        }
        this.baseDirectory = baseDirectory;
        this.baseDirectory.mkdirs();

        this.configurationFile = configurationFile;
        this.factory = factory;
        readConfiguration();
        if (tsOutputDir == null) {
            tsOutputDir = new File(baseDirectory, "target/ts");
        }

        this.workingDirectory = ConfiguredDirectory.ofOrTemporaryDir(workingDir, "jsweet");
        this.workingDir = workingDirectory.getPath().toFile();
        
        this.extractedCandyJavascriptDirectory = extractedCandiesJavascriptDir;
        this.extractedCandyJavascriptDir = extractedCandiesJavascriptDir == null ? null
            : extractedCandiesJavascriptDir.getPath().toFile();
        try {
            tsOutputDir.mkdirs();
            this.tsOutputDir = tsOutputDir.getCanonicalFile();
            if (jsOutputDir != null && generateJsFiles) {
                jsOutputDir.mkdirs();
                this.jsOutputDir = jsOutputDir.getCanonicalFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("cannot locate output dirs", e);
        }

        File extensionDirectory = new File(baseDirectory, JSweetConfig.EXTENSION_DIR);

        classPath = classPath == null ? System.getProperty("java.class.path") : classPath;
        classPath = extensionDirectory.getAbsolutePath() + File.pathSeparator + classPath;
        this.classPath = classPath;

        logger.info("creating transpiler version " + JSweetConfig.getVersionNumber() + " (build date: "
                + JSweetConfig.getBuildDate() + ")");
        logger.info("current dir: " + new File(".").getAbsolutePath());
        logger.info("base directory: " + this.baseDirectory.getAbsolutePath());
        logger.info("working directory: " + this.workingDir.getAbsolutePath());
        logger.info("tsOut: " + tsOutputDir + (tsOutputDir == null ? "" : " - " + tsOutputDir.getAbsolutePath()));
        logger.info("jsOut: " + jsOutputDir + (jsOutputDir == null ? "" : " - " + jsOutputDir.getAbsolutePath()));
        logger.info("candyJsOut: " + extractedCandiesJavascriptDir);
        logger.info("factory: " + factory);
        logger.debug("compile classpath: " + classPath);
        logger.debug("runtime classpath: " + System.getProperty("java.class.path"));
        logger.debug("extension directory: " + extensionDirectory.getAbsolutePath());
        this.candiesProcessor = new CandyProcessor(this.workingDir, this.tsOutputDir, classPath,
                extractedCandyJavascriptDir);

        new ExtensionManager(extensionDirectory.getAbsolutePath()).checkAndCompileExtension(this.workingDir, classPath);
    }

    /**
     * Gets this transpiler working directory (where the temporary files are
     * stored).
     */
    public File getWorkingDirectory() {
        return this.workingDir;
    }

    protected void initNode(TranspilationHandler transpilationHandler) throws Exception {
        ProcessUtil.initNode();

        File initFile = new File(workingDir, ".node-init");
        boolean initialized = initFile.exists();
        if (!initialized) {
            ProcessUtil.runCommand(ProcessUtil.NODE_COMMAND, currentNodeVersion -> {
                logger.info("node version: " + currentNodeVersion);

                if (!isVersionHighEnough(currentNodeVersion, ProcessUtil.NODE_MINIMUM_VERSION)) {
                    transpilationHandler.report(JSweetProblem.NODE_OBSOLETE_VERSION, null,
                            JSweetProblem.NODE_OBSOLETE_VERSION.getMessage(currentNodeVersion,
                                    ProcessUtil.NODE_MINIMUM_VERSION));
                    // throw new RuntimeException("node.js version is obsolete,
                    // minimum version: " + ProcessUtil.NODE_MINIMUM_VERSION);
                }

            }, () -> {
                transpilationHandler.report(JSweetProblem.NODE_CANNOT_START, null,
                        JSweetProblem.NODE_CANNOT_START.getMessage());
                throw new RuntimeException("cannot find node.js");
            }, "--version");
            initFile.mkdirs();
            initFile.createNewFile();
        }

        String v = "";
        File tscVersionFile = new File(ProcessUtil.NPM_DIR, "tsc-version");
        if (tscVersionFile.exists()) {
            v = FileUtils.readFileToString(tscVersionFile);
        }
        if (!ProcessUtil.isExecutableInstalledGloballyWithNpm("tsc") || !v.trim().startsWith(TSC_VERSION)) {
            // this will lead to performances issues if having multiple versions
            // of JSweet installed
            if (ProcessUtil.isExecutableInstalledGloballyWithNpm("tsc")) {
                ProcessUtil.uninstallGlobalNodePackage("typescript");
            }
            ProcessUtil.installGlobalNodePackage("typescript", TSC_VERSION);
            FileUtils.writeStringToFile(tscVersionFile, TSC_VERSION);
        }
    }

    /**
     * Sets one or more directories that contain TypeScript definition files
     * (sub-directories are scanned recursively to find all .d.ts files).
     * 
     * @param tsDefDirs a list of directories to scan for .d.ts files
     */
    public void setTsDefDirs(File... tsDefDirs) {
        clearTsDefDirs();
        this.tsDefDirs.addAll(Arrays.asList(tsDefDirs));
    }

    /**
     * Adds a directory that contains TypeScript definition files (sub-directories
     * are scanned recursively to find all .d.ts files).
     * 
     * @param tsDefDir a directory to scan for .d.ts files
     */
    public void addTsDefDir(File tsDefDir) {
        if (!tsDefDirs.contains(tsDefDir)) {
            tsDefDirs.add(tsDefDir);
        }
    }

    /**
     * Undo previous calls to {@link #setTsDefDirs(File...)} and
     * {@link #addTsDefDir(File)}.
     */
    public void clearTsDefDirs() {
        tsDefDirs.clear();
    }

    private boolean areAllTranspiled(SourceFile... sourceFiles) {
        for (SourceFile file : sourceFiles) {
            if (file.getJsFile() == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Evaluates the given Java source files with the default JavaScript engine
     * (Nashorn).
     * <p>
     * This function automatically transpile the source files if needed.
     * 
     * @param transpilationHandler the transpilation handler
     * @param sourceFiles          the source files to be evaluated
     * @return an object that holds the evaluation result
     * @throws Exception when an internal error occurs
     */
    public EvaluationResult eval(TranspilationHandler transpilationHandler, SourceFile... sourceFiles)
            throws Exception {
        return eval("JavaScript", transpilationHandler, sourceFiles);
    }

    /**
     * Evaluates the given source files with the given evaluation engine.
     * <p>
     * If given engine name is "Java", this function looks up for the classes in the
     * classpath and run the main methods when found.
     * 
     * @param engineName           the engine name: either "Java" or any valid and
     *                             installed JavaScript engine.
     * @param transpilationHandler the log handler
     * @param sourceFiles          the source files to be evaluated (transpiled
     *                             first if needed)
     * @return the evaluation result
     * @throws Exception when an internal error occurs
     */
    public EvaluationResult eval(String engineName, TranspilationHandler transpilationHandler,
            SourceFile... sourceFiles) throws Exception {
        logger.info("[" + engineName + " engine] eval files: " + Arrays.asList(sourceFiles));

        if ("Java".equals(engineName)) {

            JavaEval evaluator = new JavaEval(this, new EvalOptions(isUsingModules(), workingDir, null));
            return evaluator.performEval(sourceFiles);
        } else {
            if (!areAllTranspiled(sourceFiles)) {
                ErrorCountTranspilationHandler errorHandler = new ErrorCountTranspilationHandler(transpilationHandler);
                transpile(errorHandler, Collections.emptySet(), sourceFiles);
                if (errorHandler.getErrorCount() > 0) {
                    throw new Exception("unable to evaluate: transpilation errors remain");
                }

            }

            Collection<File> jsFiles;
            if (context.useModules) {
                File f = null;
                if (!context.entryFiles.isEmpty()) {
                    f = context.entryFiles.get(0);
                    for (SourceFile sf : sourceFiles) {
                        if (sf.getJavaFile().equals(f)) {
                            f = sf.getJsFile();
                        }
                    }
                }
                if (f == null) {
                    f = sourceFiles[sourceFiles.length - 1].getJsFile();
                }
                jsFiles = asList(f);
            } else {
                jsFiles = Stream.of(sourceFiles).map(sourceFile -> sourceFile.getJsFile()).collect(toList());
            }

            JavaScriptEval evaluator = new JavaScriptEval(
                    new EvalOptions(isUsingModules(), workingDir, context.getUsingJavaRuntime()),
                    JavaScriptRuntime.NodeJs);
            return evaluator.performEval(jsFiles);
        }
    }

    public JSweetContext prepareForJavaFiles(List<File> javaFiles, ErrorCountTranspilationHandler transpilationHandler)
            throws IOException {

        transpilationHandler.setDisabled(isIgnoreJavaErrors());

        context = factory.createContext(this);
        context.setUsingJavaRuntime(forceJavaRuntime ? javaRuntimeJ4TsJs
                : (candiesProcessor == null ? null : candiesProcessor.getUsingJavaRuntime()));
        context.useModules = isUsingModules();
        context.useRequireForModules = moduleKind != ModuleKind.es2015;
        
        if (context.useModules && bundle) {
            context.useModules = false;
            context.moduleBundleMode = true;
        }

        JavaCompilationComponents.Options javaCompilationOptions = new JavaCompilationComponents.Options();
        javaCompilationOptions.classPath = getClassPath();
        javaCompilationOptions.encoding = getEncoding();
        javaCompilationOptions.transpilationHandler = transpilationHandler;
        if (getJavaCompilerExtraOptions() != null && getJavaCompilerExtraOptions().length > 1) {
        	logger.info("extra Java compiler options: " + Arrays.asList(getJavaCompilerExtraOptions()));
        	for (int i = 0; i < getJavaCompilerExtraOptions().length - 1; i += 2) {
            	javaCompilationOptions.extraOptions.put(getJavaCompilerExtraOptions()[i], getJavaCompilerExtraOptions()[i + 1]);
        	}
        }
        javaCompilationComponents = JavaCompilationComponents.prepareFor(javaFiles, context, factory,
                javaCompilationOptions);

        Iterable<? extends CompilationUnitTree> compilUnits;
        if (javaFiles.size() > 0) {
            compilUnits = javaCompilationComponents.getTask().parse();
            javaCompilationComponents.getTask().analyze();
        } else {
            compilUnits = new ArrayList<CompilationUnitTree>();
        }

        transpilationHandler.setDisabled(false);
        context.compilationUnits = util().iterableToList(compilUnits);
        
        adapter = factory.createAdapter(context);

        if (transpilationHandler.getErrorCount() > 0) {
            return null;
        }
        if (!generateTsFiles) {
            return null;
        }

        return context;
    }

    private String ts2js(ErrorCountTranspilationHandler handler, String tsCode, String targetFileName)
            throws IOException {
        SourceFile sf = new SourceFile(null);
        sf.setTsFile(File.createTempFile(targetFileName, ".ts", tsOutputDir));
        sf.setJsFile(File.createTempFile(targetFileName, ".js", jsOutputDir));
        try {
            sf.tsFile.getParentFile().mkdirs();
            sf.tsFile.createNewFile();
            Files.write(sf.tsFile.toPath(), Arrays.asList(tsCode));
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }

        ts2js(handler, new SourceFile[] { sf });
        try {
            return new String(Files.readAllBytes(sf.jsFile.toPath()));
        } catch (IOException ex) {
            return null;
        }
    }

    /**
     * Transpiles the given Java source files. When the transpiler is in watch mode
     * ({@link #setTscWatchMode(boolean)}), the first invocation to this method
     * determines the files to be watched by the Tsc process.
     * 
     * @param transpilationHandler the log handler
     * @param files                the files to be transpiled
     * @throws IOException
     */
    synchronized public void transpile(TranspilationHandler transpilationHandler, SourceFile... files)
            throws IOException {
        transpile(transpilationHandler, Collections.emptySet(), files);
    }

    /**
     * Transpiles the given Java source files. When the transpiler is in watch mode
     * ({@link #setTscWatchMode(boolean)}), the first invocation to this method
     * determines the files to be watched by the Tsc process.
     * 
     * @param transpilationHandler the log handler
     * @param excludedSourcePaths  these files will be used in the transpilation
     *                             process but will not generated any corresponding
     *                             transpiled artefacts
     * @param files                the files to be transpiled
     * @throws IOException
     */
    synchronized public void transpile(TranspilationHandler transpilationHandler, Set<String> excludedSourcePaths,
            SourceFile... files) throws IOException {
        transpilationStartTimestamp = System.currentTimeMillis();

        SourceFile.touch(files);

        try {
            initNode(transpilationHandler);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return;
        }
        candiesProcessor.processCandies(transpilationHandler);

        if (!isIgnoreCandiesTypeScriptDefinitions()) {
            addTsDefDir(candiesProcessor.getCandiesTsdefsDir());
        }

        ErrorCountTranspilationHandler errorHandler = new ErrorCountTranspilationHandler(transpilationHandler);
        Collection<SourceFile> jsweetSources = asList(files).stream() //
                .filter(source -> source.getJavaFile() != null).collect(toList());
        if (isIgnoreJavaErrors()) {
            errorHandler.report(JSweetProblem.USER_WARNING, null,
                    "Java compilation errors are ignored - make sure you validate your Java code another way in order to avoid subsequent transpilation errors");
        }

        long startJava2TsTimeNanos = System.nanoTime();
        java2ts(errorHandler, excludedSourcePaths, jsweetSources.toArray(new SourceFile[0]));
        long endJava2TsTimeNanos = System.nanoTime();

        long startTs2JsTimeNanos = System.nanoTime();
        if (errorHandler.getErrorCount() == 0 && generateTsFiles && generateJsFiles) {
            ts2js(errorHandler, files);
        }
        long endTs2JsTimeNanos = System.nanoTime();

        if (!generateJsFiles || !generateTsFiles) {
            transpilationHandler.onCompleted(this, !isTscWatchMode(), files);
        }

        logger.info("transpilation process finished in " + (System.currentTimeMillis() - transpilationStartTimestamp)
                + " ms \n" //
                + "> java2ts: " + ((endJava2TsTimeNanos - startJava2TsTimeNanos) / 1e6) + "ms\n" + "> ts2js: "
                + ((endTs2JsTimeNanos - startTs2JsTimeNanos) / 1e6) + "ms\n");
    }

    private void ts2js(ErrorCountTranspilationHandler transpilationHandler, SourceFile[] sourceFiles)
            throws IOException {

        LinkedHashSet<SourceFile> tsSourceFiles = new LinkedHashSet<>();
        for (SourceFile sourceFile : sourceFiles) {
            if (sourceFile.getTsFile() != null) {
                tsSourceFiles.add(sourceFile);
            }
        }

        logger.info("ts2js on " + ts2jsTranspiler + " sourceFiles=" + sourceFiles.length);
        ts2jsTranspiler.ts2js(transpilationHandler, //
                tsSourceFiles, //
                tsDefDirs, //
                this, //
                isIgnoreTypeScriptErrors(), //
                this::onTsTranspilationCompleted, //
                getHangingTscTimeout());
    }

    public void setUseTsserver(boolean useTsserver) {
        if (useTsserver) {
            this.ts2jsTranspiler = TypeScript2JavaScriptWithTsserverTranspiler.INSTANCE;
        } else {
            this.ts2jsTranspiler = new TypeScript2JavaScriptWithTscTranspiler();
        }
    }

    private void java2ts(ErrorCountTranspilationHandler transpilationHandler, Set<String> excludedSourcePaths,
            SourceFile[] files) throws IOException {
        prepareForJavaFiles(Arrays.asList(SourceFile.toFiles(files)), transpilationHandler);
        if (context.compilationUnits == null) {
            return;
        }

        if (candiesProcessor.hasDeprecatedCandy()) {
            context.deprecatedApply = true;
            logger.warn("\n\n\n*********************************************************************\n" //
                    + "*********************************************************************\n" //
                    + " YOUR CLASSPATH CONTAINS JSweet v1.x CANDIES \n" //
                    + " This can lead to unexpected behaviors, please contribute to https://github.com/jsweet-candies \n" //
                    + " to add your library's typings \n" //
                    + "*********************************************************************\n" //
                    + "*********************************************************************\n\n");
        }

        context.sourceFiles = files;
        context.excludedSourcePaths = excludedSourcePaths;

        factory.createBeforeTranslationScanner(transpilationHandler, context).process(context.compilationUnits);

        if (context.useModules) {
            StaticInitilializerAnalyzer analizer = new StaticInitilializerAnalyzer(context);
            analizer.process(context.compilationUnits);
            generateTsFiles(transpilationHandler, files, context.compilationUnits);
        } else {
            if (bundle) {
                generateTsBundle(transpilationHandler, files, context.compilationUnits);
            } else {
                generateTsFiles(transpilationHandler, files, context.compilationUnits);
            }
        }
    }

    private void generateModuleDefs(CompilationUnitTree moduleDefs) throws IOException {
        StringBuilder out = new StringBuilder();
        for (String line : FileUtils.readLines(new File(moduleDefs.getSourceFile().getName()))) {
            if (line.startsWith("///")) {
                out.append(line.substring(3));
            }
        }
        FileUtils.write(new File(tsOutputDir, "module_defs.d.ts"), out, false);
    }

    private void generateTsFiles(ErrorCountTranspilationHandler transpilationHandler, SourceFile[] files,
            List<CompilationUnitTree> compilationUnits) throws IOException {
        // regular file-to-file generation
        new OverloadScanner(transpilationHandler, context).process(compilationUnits);
        context.constAnalyzer = new ConstAnalyzer();
        context.constAnalyzer.scan(compilationUnits, context.trees);
        // 407TODO: cross variables analyzer for converting regular java variable to shared array buffer;
        context.cvsAnalyzer = new CvsAnalyzer();
        context.cvsAnalyzer.scan(compilationUnits, context.trees);
        if (isVerbose()) {
            context.dumpOverloads(System.out);
        }

        if (isAutoPropagateAsyncAwaits()) {
            new AsyncAwaitPropagationScanner(context).process(compilationUnits, context.trees);
        }

        adapter.onTranspilationStarted();

        String[] headerLines = getHeaderLines();
        for (int i = 0; i < compilationUnits.size(); i++) {
            if (context.isExcludedSourcePath(files[i].toString())) {
                continue;
            }

            try {
                CompilationUnitTree cu = compilationUnits.get(i);
                if (isModuleDefsFile(cu)) {
                    if (context.useModules) {
                        generateModuleDefs(cu);
                    }
                    continue;
                }
                logger.info("scanning " + cu.getSourceFile().getName() + "...");

                AbstractTreePrinter printer = factory.createTranslator(adapter, transpilationHandler, context, cu,
                        generateSourceMaps);
                printer.print(cu);
                if (StringUtils.isWhitespace(printer.getResult())) {
                    continue;
                }
                String[] s = cu.getSourceFile().getName().split(File.separator.equals("\\") ? "\\\\" : File.separator);
                String cuName = s[s.length - 1];
                s = cuName.split("\\.");
                cuName = s[0];

                String packageFullName = util().getPackageFullNameForCompilationUnit(cu);
                Element packageElement = Util.getElement(cu.getPackage());

                String javaSourceFileRelativeFullName = (packageFullName.replace(".", File.separator) + File.separator
                        + cuName + ".java");
                files[i].javaSourceDirRelativeFile = new File(javaSourceFileRelativeFullName);
                files[i].javaSourceDir = new File(cu.getSourceFile().getName().substring(0,
                        cu.getSourceFile().getName().length() - javaSourceFileRelativeFullName.length()));

                String packageName = isNoRootDirectories() ? context.getRootRelativeJavaName(packageElement)
                        : packageFullName;
                String outputFileRelativePathNoExt = packageName.replace(".", File.separator) + File.separator + cuName;
                String outputFileRelativePath = outputFileRelativePathNoExt
                        + (packageFullName.startsWith("def.") ? ".d.ts" : ".ts");
                logger.info("output file: " + outputFileRelativePath);
                File outputFile = new File(tsOutputDir, outputFileRelativePath);
                outputFile.getParentFile().mkdirs();
                String outputFilePath = outputFile.getPath();
                PrintWriter out = new PrintWriter(outputFilePath, this.outEncoding);
                String headers = context.getHeaders();
                int headersLineCount = StringUtils.countMatches(headers, "\n");
                try {
                    for (String line : headerLines) {
                        out.println(line);
                    }
                    out.print(headers);
                    out.println(printer.getResult());
                    out.print(context.getGlobalsMappingString());
                    out.print(context.getFooterStatements());
                } finally {
                    out.close();
                }
                files[i].tsFile = outputFile;
                files[i].javaFileLastTranspiled = files[i].getJavaFile().lastModified();
                printer.sourceMap.shiftOutputPositions(headerLines.length + headersLineCount);
                files[i].setSourceMap(printer.sourceMap);
                if (generateSourceMaps && !generateJsFiles) {
                    generateTypeScriptSourceMapFile(files[i]);
                }
                logger.info("created " + outputFilePath);
            } finally {
                context.clearHeaders();
                context.clearGlobalsMappings();
                context.clearFooterStatements();
            }
        }

        adapter.onTranspilationFinished();
    }

    private void generateTypeScriptSourceMapFile(SourceFile sourceFile) throws IOException {
        if (sourceFile.getSourceMap() == null) {
            return;
        }
        SourceMapGenerator generator = SourceMapGeneratorFactory.getInstance(SourceMapFormat.V3);
        String javaSourceFilePath = sourceFile.getTsFile().getAbsoluteFile().getCanonicalFile().getParentFile().toPath()
                .relativize(sourceFile.getJavaFile().getAbsoluteFile().getCanonicalFile().toPath()).toString();
        for (Entry entry : sourceFile.getSourceMap().getSortedEntries(new Comparator<SourceMap.Entry>() {
            @Override
            public int compare(Entry e1, Entry e2) {
                return e1.getOutputPosition().compareTo(e2.getOutputPosition());
            }
        })) {
            generator.addMapping(javaSourceFilePath, null,
                    new FilePosition(entry.getInputPosition().getLine(), entry.getInputPosition().getColumn()),
                    new FilePosition(entry.getOutputPosition().getLine(), entry.getOutputPosition().getColumn()),
                    new FilePosition(entry.getOutputPosition().getLine(), entry.getOutputPosition().getColumn() + 1));
        }
        File outputFile = new File(sourceFile.getTsFile().getPath() + ".map");
        try (FileWriter writer = new FileWriter(outputFile, false)) {
            generator.appendTo(writer, sourceFile.getTsFile().getName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private boolean isModuleDefsFile(CompilationUnitTree cu) {
        return cu.getSourceFile().getName().equals("module_defs.java")
                || cu.getSourceFile().getName().endsWith("/module_defs.java");
    }

    private void generateTsBundle(ErrorCountTranspilationHandler transpilationHandler, SourceFile[] files,
            List<CompilationUnitTree> compilationUnits) throws IOException {
        if (context.useModules) {
            return;
        }
        StaticInitilializerAnalyzer analizer = new StaticInitilializerAnalyzer(context);
        analizer.process(compilationUnits);
        ArrayList<Node<CompilationUnitTree>> sourcesInCycle = new ArrayList<>();
        java.util.List<CompilationUnitTree> orderedCompilationUnits = analizer.globalStaticInitializersDependencies
                .topologicalSort(n -> {
                    sourcesInCycle.add(n);
                });
        if (!sourcesInCycle.isEmpty()) {
            transpilationHandler.report(JSweetProblem.CYCLE_IN_STATIC_INITIALIZER_DEPENDENCIES, null,
                    JSweetProblem.CYCLE_IN_STATIC_INITIALIZER_DEPENDENCIES.getMessage(sourcesInCycle.stream()
                            .map(n -> n.element.getSourceFile().getName()).collect(Collectors.toList())));

            DirectedGraph.dumpCycles(sourcesInCycle, u -> u.getSourceFile().getName());

            return;
        }

        new OverloadScanner(transpilationHandler, context).process(orderedCompilationUnits);
        context.constAnalyzer = new ConstAnalyzer();
        context.constAnalyzer.scan(orderedCompilationUnits, context.trees);

        if (isAutoPropagateAsyncAwaits()) {
            new AsyncAwaitPropagationScanner(context).process(compilationUnits, context.trees);
        }

        adapter.onTranspilationStarted();

        logger.debug("ordered compilation units: " + orderedCompilationUnits.stream().map(cu -> {
            return cu.getSourceFile().getName();
        }).collect(Collectors.toList()));
        logger.debug(
                "count: " + compilationUnits.size() + " (initial), " + orderedCompilationUnits.size() + " (ordered)");
        int[] permutation = new int[orderedCompilationUnits.size()];
        StringBuilder permutationString = new StringBuilder();
        for (int i = 0; i < orderedCompilationUnits.size(); i++) {
            permutation[i] = compilationUnits.indexOf(orderedCompilationUnits.get(i));
            permutationString.append("" + i + "=" + permutation[i] + ";");
        }
        logger.debug("permutation: " + permutationString.toString());
        createBundle(transpilationHandler, files, permutation, orderedCompilationUnits, false);
        if (isGenerateDefinitions()) {
            createBundle(transpilationHandler, files, permutation, orderedCompilationUnits, true);
        }

        adapter.onTranspilationFinished();
    }

    private void initSourceFileJavaPaths(SourceFile file, CompilationUnitTree cu) {
        String[] s = cu.getSourceFile().getName().split(File.separator.equals("\\") ? "\\\\" : File.separator);
        String cuName = s[s.length - 1];
        s = cuName.split("\\.");
        cuName = s[0];

        String javaSourceFileRelativeFullName = (util().getPackageFullNameForCompilationUnit(cu).replace(".",
                File.separator) + File.separator + cuName + ".java");
        file.javaSourceDirRelativeFile = new File(javaSourceFileRelativeFullName);
        file.javaSourceDir = new File(cu.getSourceFile().getName().substring(0,
                cu.getSourceFile().getName().length() - javaSourceFileRelativeFullName.length()));
    }

    private void createBundle(ErrorCountTranspilationHandler transpilationHandler, SourceFile[] files,
            int[] permutation, java.util.List<CompilationUnitTree> orderedCompilationUnits, boolean definitionBundle)
            throws FileNotFoundException, UnsupportedEncodingException {
        context.bundleMode = true;
        StringBuilder sb = new StringBuilder();
        int lineCount = 0;
        for (String line : getHeaderLines()) {
            sb.append(line).append("\n");
            lineCount++;
        }

        ArrayList<SourceFile> bundledFiles = new ArrayList<>();
        for (int i = 0; i < orderedCompilationUnits.size(); i++) {
            CompilationUnitTree cu = orderedCompilationUnits.get(i);
            if (isModuleDefsFile(cu)) {
                continue;
            }
            if (util().getPackageFullNameForCompilationUnit(cu).startsWith("def.")) {
                if (!definitionBundle) {
                    continue;
                }
            } else {
                if (definitionBundle) {
                    continue;
                }
            }
            logger.info("scanning " + cu.getSourceFile().getName() + "...");
            AbstractTreePrinter printer = factory.createTranslator(adapter, transpilationHandler, context, cu,
                    generateSourceMaps);
            printer.print(cu);
            printer.sourceMap.shiftOutputPositions(lineCount);
            files[permutation[i]].setSourceMap(printer.sourceMap);

            bundledFiles.add(files[permutation[i]]);

            sb.append(printer.getOutput());
            lineCount += (printer.getCurrentLine() - 1);

            initSourceFileJavaPaths(files[permutation[i]], cu);
        }

        context.bundleMode = false;

        File bundleDirectory = tsOutputDir;
        if (!bundleDirectory.exists()) {
            bundleDirectory.mkdirs();
        }
        String bundleName = "bundle" + (definitionBundle ? ".d.ts" : ".ts");

        File outputFile = new File(bundleDirectory, bundleName);

        logger.info("creating bundle file: " + outputFile);
        outputFile.getParentFile().mkdirs();
        String outputFilePath = outputFile.getPath();
        PrintWriter out = new PrintWriter(outputFilePath, this.outEncoding);
        try {
            String headers = context.getHeaders();
            out.print(headers);
            lineCount = StringUtils.countMatches(headers, "\n");
            for (SourceFile f : bundledFiles) {
                f.getSourceMap().shiftOutputPositions(lineCount);
            }

            out.println(sb.toString());
            if (!definitionBundle) {
                out.print(context.getGlobalsMappingString());
            }
            out.print(context.getFooterStatements());
            context.clearFooterStatements();
            if (definitionBundle && context.getExportedElements() != null) {
                for (Map.Entry<String, List<Element>> exportedElements : context.getExportedElements().entrySet()) {
                    out.println();
                    out.print("declare module \"" + exportedElements.getKey() + "\"");
                    boolean exported = false;
                    for (Element element : exportedElements.getValue()) {
                        if (element instanceof PackageElement && !context.isRootPackage(element)) {
                            out.print(" {");
                            out.println();
                            out.print("    export = " + context.getExportedElementName(element) + ";");
                            out.println();
                            out.print("}");
                            exported = true;
                            break;
                        }
                    }
                    if (!exported) {
                        out.print(";");
                    }
                    out.println();
                }
            }
        } finally {
            out.close();
        }
        for (int i = 0; i < orderedCompilationUnits.size(); i++) {
            CompilationUnitTree cu = orderedCompilationUnits.get(i);
            if (util().getPackageFullNameForCompilationUnit(cu).startsWith("def.")) {
                if (!definitionBundle) {
                    continue;
                }
            } else {
                if (definitionBundle) {
                    continue;
                }
            }
            files[permutation[i]].tsFile = outputFile;
            files[permutation[i]].javaFileLastTranspiled = files[permutation[i]].getJavaFile().lastModified();
        }
        logger.info("created " + outputFilePath);

    }

    private ConfiguredDirectory extractedCandyJavascriptDirectory;
    private File extractedCandyJavascriptDir;

    /**
     * Returns the watched files when the transpiler is in watch mode. See
     * {@link #setTscWatchMode(boolean)}. The watched file list corresponds to the
     * one given at the first invocation of
     * {@link #transpile(TranspilationHandler, SourceFile...)} after the transpiler
     * was set to watch mode. All subsequent invocations of
     * {@link #transpile(TranspilationHandler, SourceFile...)} will not change the
     * initial watched files. In order to change the watch files, invoke
     * {@link #resetTscWatchMode()} and call
     * {@link #transpile(TranspilationHandler, SourceFile...)} with a new file list.
     */
    synchronized public SourceFile[] getWatchedFiles() {
        // TODO : watch mode isn't implemented with tsserver transpiler, either we
        // remove watch mode (only used for tests if I'm right), or provide a dummy
        // implementation for tsserver
        return ((TypeScript2JavaScriptWithTscTranspiler) ts2jsTranspiler).getWatchedFiles().toArray(new SourceFile[0]);
    }

    /**
     * Gets the watched files that corresponds to the given Java file. See
     * {@link #setTscWatchMode(boolean)}.
     */
    synchronized public SourceFile getWatchedFile(File javaFile) {
        // TODO : see #getWatchedFiles()
        return ((TypeScript2JavaScriptWithTscTranspiler) ts2jsTranspiler).getWatchedFile(javaFile);
    }

    /**
     * Tells if this transpiler is using a Tsc watch process to automatically
     * regenerate the javascript when one of the source file changes.
     */
    synchronized public boolean isTscWatchMode() {
        return tscWatchMode;
    }

    /**
     * Enables or disable this transpiler watch mode. When watch mode is enabled,
     * the first invocation to
     * {@link #transpile(TranspilationHandler, SourceFile...)} will start the Tsc
     * watch process, which regenerates the JavaScript files when one of the input
     * file changes.
     * 
     * @param tscWatchMode true: enables the watch mode (do nothing is already
     *                     enabled), false: disables the watch mode and stops the
     *                     current Tsc watching process
     * @see #getWatchedFile(File)
     */
    synchronized public void setTscWatchMode(boolean tscWatchMode) {
        this.tscWatchMode = tscWatchMode;
        if (!tscWatchMode) {
            // TODO : see #getWatchedFiles()
            ((TypeScript2JavaScriptWithTscTranspiler) ts2jsTranspiler).stopWatch();
        }
    }

    private void onTsTranspilationCompleted(boolean fullPass, ErrorCountTranspilationHandler handler,
            Collection<SourceFile> files) {
        try {
            if (isGenerateDeclarations()) {
                if (getDeclarationsOutputDir() != null) {
                    logger.info("moving d.ts files to " + getDeclarationsOutputDir());
                    LinkedList<File> dtsFiles = new LinkedList<File>();
                    File rootDir = jsOutputDir == null ? tsOutputDir : jsOutputDir;
                    util().addFiles(".d.ts", rootDir, dtsFiles);
                    for (File dtsFile : dtsFiles) {
                        String relativePath = util().getRelativePath(rootDir.getAbsolutePath(),
                                dtsFile.getAbsolutePath());
                        File targetFile = new File(getDeclarationsOutputDir(), relativePath);
                        logger.info("moving " + dtsFile + " to " + targetFile);
                        if (targetFile.exists()) {
                            FileUtils.deleteQuietly(targetFile);
                        }
                        try {
                            FileUtils.moveFile(dtsFile, targetFile);
                        } catch (Exception e) {
                            logger.error(e.getMessage(), e);
                        }
                    }
                }
            }
            if (handler.getErrorCount() == 0) {
                Set<File> handledFiles = new HashSet<>();
                for (SourceFile sourceFile : files) {
                    if (!sourceFile.getTsFile().getAbsolutePath().startsWith(tsOutputDir.getAbsolutePath())) {
                        throw new RuntimeException("ts directory isn't configured properly, please use setTsDir: "
                                + sourceFile.getTsFile().getAbsolutePath() + " != " + tsOutputDir.getAbsolutePath());
                    }
                    String outputFileRelativePath = sourceFile.getTsFile().getAbsolutePath()
                            .substring(tsOutputDir.getAbsolutePath().length());
                    File outputFile = new File(jsOutputDir == null ? tsOutputDir : jsOutputDir,
                            util().removeExtension(outputFileRelativePath) + ".js");
                    sourceFile.jsFile = outputFile;
                    if (outputFile.lastModified() > sourceFile.jsFileLastTranspiled) {
                        if (handledFiles.contains(outputFile)) {
                            continue;
                        }
                        handledFiles.add(outputFile);
                        logger.info("js output file: " + outputFile);
                        File mapFile = new File(outputFile.getAbsolutePath() + ".map");

                        if (mapFile.exists() && generateSourceMaps) {

                            SourceMapGeneratorV3 generator = (SourceMapGeneratorV3) SourceMapGeneratorFactory
                                    .getInstance(SourceMapFormat.V3);
                            Path javaSourcePath = sourceFile.javaSourceDir.getCanonicalFile().toPath();
                            String sourceRoot = getSourceRoot() != null ? getSourceRoot().toString()
                                    : sourceFile.getJsFile().getParentFile().getCanonicalFile().toPath()
                                            .relativize(javaSourcePath) + "/";
                            generator.setSourceRoot(sourceRoot);

                            sourceFile.jsMapFile = mapFile;
                            logger.info("redirecting map file: " + mapFile);
                            String contents = FileUtils.readFileToString(mapFile);
                            SourceMapping mapping = SourceMapConsumerFactory.parse(contents);

                            int line = 1;
                            int columnIndex = 0;
                            for (String lineContent : FileUtils.readLines(outputFile, (Charset) null)) {
                                columnIndex = 0;
                                while (columnIndex < lineContent.length() && (lineContent.charAt(columnIndex) == ' '
                                        || lineContent.charAt(columnIndex) == '\t')) {
                                    columnIndex++;
                                }

                                OriginalMapping originalMapping = mapping.getMappingForLine(line, columnIndex + 1);
                                if (originalMapping != null) {
                                    // TODO: this is quite slow and should be
                                    // optimized
                                    SourcePosition originPosition = SourceFile.findOriginPosition(new SourcePosition(
                                            sourceFile.tsFile, null, new Position(originalMapping.getLineNumber(),
                                                    originalMapping.getColumnPosition())),
                                            files);
                                    if (originPosition != null) {
                                        // as a first approximation, we only map
                                        // line numbers (ignore columns)
                                        generator.addMapping(
                                                javaSourcePath
                                                        .relativize(
                                                                originPosition.getFile().getCanonicalFile().toPath())
                                                        .toString(),
                                                null, new FilePosition(originPosition.getStartLine() - 1, 0),
                                                new FilePosition(line - 1, 0),
                                                new FilePosition(line - 1, lineContent.length() - 1));
                                    }
                                }

                                line++;
                            }

                            try (FileWriter writer = new FileWriter(mapFile, false)) {
                                generator.appendTo(writer, outputFile.getName());
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            handler.onCompleted(this, fullPass, files.toArray(new SourceFile[0]));
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jsweet.transpiler.JSweetOptions#isPreserveSourceLineNumbers()
     */
    @Deprecated
    @Override
    public boolean isPreserveSourceLineNumbers() {
        return generateSourceMaps;
    }

    @Override
    public boolean isGenerateSourceMaps() {
        return generateSourceMaps;
    }

    /**
     * Sets the flag that tells if the transpiler preserves the generated TypeScript
     * source line numbers wrt the Java original source file (allows for Java
     * debugging through js.map files).
     * 
     * @deprecated use {@link #setGenerateSourceMaps(boolean)} instead
     */
    @Deprecated
    public void setPreserveSourceLineNumbers(boolean preserveSourceLineNumbers) {
        this.generateSourceMaps = preserveSourceLineNumbers;
    }

    /**
     * Sets the flag that tells if the transpiler allows for Java debugging through
     * js.map files.
     */
    public void setGenerateSourceMaps(boolean generateSourceMaps) {
        this.generateSourceMaps = generateSourceMaps;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jsweet.transpiler.JSweetOptions#getTsOutputDir()
     */
    @Override
    public File getTsOutputDir() {
        return tsOutputDir;
    }

    /**
     * Sets the current TypeScript output directory.
     */
    public void setTsOutputDir(File tsOutputDir) {
        this.tsOutputDir = tsOutputDir;
        this.candiesProcessor.setTsOutputDir(tsOutputDir);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jsweet.transpiler.JSweetOptions#getJsOutputDir()
     */
    @Override
    public File getJsOutputDir() {
        return jsOutputDir;
    }

    /**
     * Sets the current JavaScript output directory.
     */
    public void setJsOutputDir(File jsOutputDir) {
        this.jsOutputDir = jsOutputDir;
    }

    /**
     * Tells if the JavaScript generation is enabled/disabled.
     */
    @Override
    public boolean isGenerateJsFiles() {
        return generateJsFiles;
    }

    /**
     * Sets the flag to enable/disable JavaScript generation.
     */
    public void setGenerateJsFiles(boolean generateJsFiles) {
        this.generateJsFiles = generateJsFiles;
    }

    /**
     * Resets the watch mode (clears the watched files and restarts the Tsc process
     * on the next invocation of
     * {@link #transpile(TranspilationHandler, SourceFile...)}).
     */
    synchronized public void resetTscWatchMode() {
        setTscWatchMode(false);
        setTscWatchMode(true);
    }

    /**
     * Gets the candies processor.
     */
    public CandyProcessor getCandiesProcessor() {
        return candiesProcessor;
    }

    /**
     * Sets target ECMA script version for generated JavaScript
     * 
     * @param ecmaTargetVersion The target version
     */
    public void setEcmaTargetVersion(EcmaScriptComplianceLevel ecmaTargetVersion) {
        this.ecmaTargetVersion = ecmaTargetVersion;
    }

    @Override
    public EcmaScriptComplianceLevel getEcmaTargetVersion() {
        return ecmaTargetVersion;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jsweet.transpiler.JSweetOptions#getModuleKind()
     */
    @Override
    public ModuleKind getModuleKind() {
        return moduleKind;
    }

    /**
     * Sets the module kind when transpiling to code using JavaScript modules.
     */
    public void setModuleKind(ModuleKind moduleKind) {
        this.moduleKind = moduleKind;
    }

    @Override
    public ModuleResolution getModuleResolution() {
        return moduleResolution;
    }

    /**
     * Sets the module strategy when transpiling to code using JavaScript modules.
     */
    public void setModuleResolution(ModuleResolution moduleResolution) {
        this.moduleResolution = moduleResolution;
    }

    /**
     * Tells tsc to skip some checks in order to reduce load time, useful in unit
     * tests where transpiler is invoked many times
     */
    public void setSkipTypeScriptChecks(boolean skipTypeScriptChecks) {
        this.skipTypeScriptChecks = skipTypeScriptChecks;
    }

    @Override
    public boolean isSkipTypeScriptChecks() {
        return skipTypeScriptChecks;
    }

    /**
     * Tells if this transpiler transpiles to code using JavaScript modules.
     */
    public boolean isUsingModules() {
        return moduleKind != null && moduleKind != ModuleKind.none;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jsweet.transpiler.JSweetOptions#isBundle()
     */
    @Override
    public boolean isBundle() {
        return bundle;
    }

    /**
     * Sets this transpiler to generate JavaScript bundles for running in a Web
     * browser.
     */
    public void setBundle(boolean bundle) {
        this.bundle = bundle;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jsweet.transpiler.JSweetOptions#getEncoding()
     */
    @Override
    public String getEncoding() {
        return encoding;
    }

    /**
     * Sets the expected Java source code encoding.
     */
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    @Override
    public String getOutEncoding() {
        return outEncoding;
    }

    /**
     * Sets the encoding for the generated TypeScript code.
     */
    public void setOutEncoding(String encoding) {
        this.outEncoding = encoding;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jsweet.transpiler.JSweetOptions#isNoRootDirectories()
     */
    @Override
    public boolean isNoRootDirectories() {
        return noRootDirectories;
    }

    /**
     * Sets this transpiler to skip the root directories (packages annotated
     * with @jsweet.lang.Root) so that the generated file hierarchy starts at the
     * root directories rather than including the entire directory structure.
     */
    public void setNoRootDirectories(boolean noRootDirectories) {
        this.noRootDirectories = noRootDirectories;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jsweet.transpiler.JSweetOptions#isIgnoreAssertions()
     */
    @Override
    public boolean isIgnoreAssertions() {
        return ignoreAssertions;
    }

    /**
     * Sets the transpiler to ignore the 'assert' statements or generate appropriate
     * code.
     */
    public void setIgnoreAssertions(boolean ignoreAssertions) {
        this.ignoreAssertions = ignoreAssertions;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jsweet.transpiler.JSweetOptions#isIgnoreJavaFileNameError()
     */
    @Override
    public boolean isIgnoreJavaFileNameError() {
        return ignoreJavaFileNameError;
    }

    public void setIgnoreJavaFileNameError(boolean ignoreJavaFileNameError) {
        this.ignoreJavaFileNameError = ignoreJavaFileNameError;
    }

    @Override
    public boolean isGenerateDeclarations() {
        return generateDeclarations;
    }

    public void setGenerateDeclarations(boolean generateDeclarations) {
        this.generateDeclarations = generateDeclarations;
    }

    @Override
    public File getDeclarationsOutputDir() {
        return declarationsOutputDir;
    }

    public void setDeclarationsOutputDir(File declarationsOutputDir) {
        this.declarationsOutputDir = declarationsOutputDir;
    }

    @Override
    public File getExtractedCandyJavascriptDir() {
        return extractedCandyJavascriptDir;
    }

    /**
     * Add JavaScript libraries that are used for the JavaScript evaluation.
     * 
     * @see #eval(TranspilationHandler, SourceFile...)
     */
    public void addJsLibFiles(File... files) {
        jsLibFiles.addAll(Arrays.asList(files));
    }

    /**
     * Clears JavaScript libraries that are used for the JavaScript evaluation.
     * 
     * @see #eval(TranspilationHandler, SourceFile...)
     */
    public void clearJsLibFiles() {
        jsLibFiles.clear();
    }

    /**
     * Transpiles the given Java AST.
     * 
     * @param transpilationHandler the log handler
     * @param tree                 the AST to be transpiled
     * @param targetFileName       the name of the file (without any extension)
     *                             where to put the transpilation output
     * @throws IOException
     */
    public String transpile(ErrorCountTranspilationHandler handler, Tree tree, String targetFileName)
            throws IOException {
        Java2TypeScriptTranslator translator = factory.createTranslator(adapter, handler, context, null, false);
        translator.enterScope();
        translator.scan(tree, context.trees);
        translator.exitScope();
        String tsCode = translator.getResult();
        return ts2js(handler, tsCode, targetFileName);
    }

    @Override
    public boolean isGenerateDefinitions() {
        return generateDefinitions;
    }

    public void setGenerateDefinitions(boolean generateDefinitions) {
        this.generateDefinitions = generateDefinitions;
    }

    @Override
    public File getSourceRoot() {
        return sourceRoot;
    }

    public void setSourceRoot(File sourceRoot) {
        this.sourceRoot = sourceRoot;
    }

    @Override
    public Map<String, Object> getConfiguration() {
        return configuration;
    }

    @Override
    public boolean isIgnoreTypeScriptErrors() {
        return ignoreTypeScriptErrors;
    }

    public void setIgnoreTypeScriptErrors(boolean ignoreTypeScriptErrors) {
        this.ignoreTypeScriptErrors = ignoreTypeScriptErrors;
    }

    @Override
    public File getHeaderFile() {
        return headerFile;
    }

    public void setHeaderFile(File headerFile) {
        this.headerFile = headerFile;
    }

    private String[] getHeaderLines() {
        String[] headerLines = null;
        if (getHeaderFile() != null) {
            try {
                headerLines = FileUtils.readLines(getHeaderFile(), getEncoding()).toArray(new String[0]);
            } catch (Exception e) {
                logger.error("cannot read header file: " + getHeaderFile() + " - using default header");
            }
        }
        if (headerLines == null) {
            headerLines = new String[] { "/* Generated from Java with JSweet " + JSweetConfig.getVersionNumber()
                    + " - http://www.jsweet.org */" };
        }
        if (context.options.isDebugMode()) {
            headerLines = ArrayUtils.add(headerLines,
                    "declare function __debug_exec(className, functionName, argNames, target, args, generator);");
            headerLines = ArrayUtils.add(headerLines, "declare function __debug_result(expression);");
        }
        return headerLines;
    }

    @Override
    public boolean isGenerateTsFiles() {
        return generateTsFiles;
    }

    public void setGenerateTsFiles(boolean generateTsFiles) {
        this.generateTsFiles = generateTsFiles;
    }

    @Override
    public boolean isIgnoreJavaErrors() {
        return ignoreJavaErrors;
    }

    public void setIgnoreJavaErrors(boolean ignoreJavaErrors) {
        this.ignoreJavaErrors = ignoreJavaErrors;
    }

    public JSweetContext getContext() {
        return context;
    }

    @Override
    public boolean isDebugMode() {
        return debugMode;
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

    @Override
    public boolean isVerbose() {
        return LogManager.getLogger("org.jsweet").getLevel() == Level.ALL;
    }

    public void setVerbose(boolean verbose) {
        Level level = Level.WARN;
        if (verbose) {
            level = Level.ALL;
        }
        LogManager.getLogger("org.jsweet").setLevel(level);
    }

    @Override
    public boolean isDisableSinglePrecisionFloats() {
        return disableSingleFloatPrecision;
    }

    public void setDisableSinglePrecisionFloats(boolean disableSinglePrecisionFloats) {
        this.disableSingleFloatPrecision = disableSinglePrecisionFloats;
    }

    @Override
    public boolean isLazyInitializedStatics() {
        return lazyInitializedStatics;
    }

    public void setLazyInitializedStatics(boolean lazyInitializedStatics) {
        this.lazyInitializedStatics = lazyInitializedStatics;
    }

    @Override
    public java.util.List<String> getAdapters() {
        return adapters;
    }

    public void setAdapters(java.util.List<String> adapters) {
        this.adapters = new ArrayList<>(adapters);
    }

    @Override
    public File getConfigurationFile() {
        return configurationFile;
    }

    public String getClassPath() {
        return classPath;
    }

    public boolean isIgnoreCandiesTypeScriptDefinitions() {
        return ignoreCandiesTypeScriptDefinitions;
    }

    public void setIgnoreCandiesTypeScriptDefinitions(boolean ignoreCandiesTypeScriptDefinitions) {
        this.ignoreCandiesTypeScriptDefinitions = ignoreCandiesTypeScriptDefinitions;
    }

    @Override
    public boolean isUseSingleQuotesForStringLiterals() {
        return this.useSingleQuotesForStringLiterals;
    }

    public void setUseSingleQuotesForStringLiterals(boolean useSingleQuotesForStringLiterals) {
        this.useSingleQuotesForStringLiterals = useSingleQuotesForStringLiterals;
    }

    @Override
    public boolean isNonEnumerableTransients() {
        return this.nonEnumerableTransients;
    }

    public void setNonEnumerableTransients(boolean nonEnumerableTransients) {
        this.nonEnumerableTransients = nonEnumerableTransients;
    }

    @Override
    public int getHangingTscTimeout() {
        return this.hangingTscTimeout;
    }

    public void setHangingTscTimeout(int hangingTscTimeoutInSeconds) {
        this.hangingTscTimeout = hangingTscTimeoutInSeconds;
    }

    @Override
    public boolean isSortClassMembers() {
        return this.sortClassMembers;
    }

    public void setSortClassMembers(boolean sortClassMembers) {
        this.sortClassMembers = sortClassMembers;
    }

    @Override
    public boolean isAutoPropagateAsyncAwaits() {
        return this.autoPropagateAsyncAwaits;
    }

    public void setAutoPropagateAsyncAwaits(boolean autoPropagateAsyncAwaits) {
        this.autoPropagateAsyncAwaits = autoPropagateAsyncAwaits;
    }

    @Override
    public String[] getJavaCompilerExtraOptions() {
        return javaCompilerExtraOptions;
    }

    public void setJavaCompilerExtraOptions(String[] javaCompilerExtraOptions) {
        this.javaCompilerExtraOptions = javaCompilerExtraOptions;
    }

    @Override
    public void close() throws Exception {
        if (javaCompilationComponents != null) {
            javaCompilationComponents.close();
        }
        workingDirectory.close();
        ConfiguredDirectory d = extractedCandyJavascriptDirectory;
        if (d != null) {
          d.close();
        }
      }

    public JSweetFactory getFactory() {
        return factory;
    }

    private Util util() {
        return context.util;
    }
}
