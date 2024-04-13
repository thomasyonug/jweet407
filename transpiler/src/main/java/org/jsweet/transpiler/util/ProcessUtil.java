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
package org.jsweet.transpiler.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * A set of utilities to launch external processes from Java.
 *
 * @author Renaud Pawlak
 */
public class ProcessUtil {
    private final static Logger logger = Logger.getLogger(ProcessUtil.class);

    public static final String NODE_MINIMUM_VERSION = "4.4.0";

    private static boolean initialized = false;

    /**
     * Initializes the node command paths (OS-specific initializations).
     */
    public static void initNode() {
        if (!initialized) {
            // hack for OSX Eclipse's path issue
            if (!System.getenv("PATH").contains("/usr/local/bin") && new File("/usr/local/bin/node").exists()) {
                addExtraPath("/usr/local/bin");
                ProcessUtil.NODE_COMMAND = "/usr/local/bin/node";
                ProcessUtil.NPM_COMMAND = "/usr/local/bin/npm";
            } else if (!System.getenv("PATH").contains("/opt/local/bin") && new File("/opt/local/bin/node").exists()) {
                addExtraPath("/opt/local/bin");
                ProcessUtil.NODE_COMMAND = "/opt/local/bin/node";
                ProcessUtil.NPM_COMMAND = "/opt/local/bin/npm";
            }
            initialized = true;
        }

        logger.debug("extra path: " + ProcessUtil.EXTRA_PATH);
    }

    public static void addExtraPath(String extraPath) {
        ProcessUtil.EXTRA_PATH += extraPath + File.pathSeparator;
    }

    /**
     * A static field that stores the user home directory.
     */
    public static File USER_HOME_DIR = new File(System.getProperty("user.home"));

    /**
     * A static field that stores the JSweet npm directory.
     */
    public static File NPM_DIR = new File(USER_HOME_DIR, ".jsweet.v3-node_modules");

    private static List<String> nodeCommandsBaseNames = Arrays.asList("tsc", "browserify", "phantomjs");

    /**
     * The node command name (can be full path in some environments).
     */
    public static String NODE_COMMAND = "node";

    /**
     * The npm command name (can be full path in some environments).
     */
    public static String NPM_COMMAND = "npm";

    /**
     * Some extra paths to be added to the PATH environment variable in some
     * environments. Typically Eclipse on Mac OSX misses the /usr/local/bin path,
     * which is required to run node.
     */
    private static String EXTRA_PATH = "";

    /**
     * Gets the full path of a global package's (system) command installed with npm.
     */
    public static String getGlobalNpmPackageNodeMainFilePath(String nodeModule, String mainFileName) {
        return NPM_DIR.getPath() + File.separator + "node_modules" + File.separator + nodeModule + File.separator
                + "bin" + File.separator + mainFileName;
    }

    /**
     * Gets the full path of a global package's JS main file installed with npm.
     * Will throw error if executable cannot be found
     */
    public static String getGlobalNpmPackageExecutablePath(String command) {
        return getGlobalNpmPackageExecutablePath(command, true);
    }

    /**
     * Gets the full path of a global package's JS main file installed with npm.
     *
     * @param throwError if false, returns null if not found instead of throwing
     *                   error
     */
    public static String getGlobalNpmPackageExecutablePath(String command, boolean throwError) {
        File commandFile = new File(command);
        if (commandFile.isFile() && commandFile.isAbsolute()) {
            return command;
        }
        if (isWindows()) {
            return NPM_DIR.getPath() + File.separator + command + ".cmd";
        } else {
            String unixPath = NPM_DIR.getPath() + File.separator + "bin" + File.separator + command;
            if (new File(unixPath).isFile()) {
                return unixPath;
            }

            unixPath = NPM_DIR.getPath() + File.separator + "node_modules" + File.separator + ".bin" + File.separator + command;
            if (new File(unixPath).isFile()) {
                return unixPath;
            }

            logger.info("cannot find " + command + " - searching in " + NPM_DIR.getAbsolutePath());

            if (throwError) {
                return lookupGlobalNpmPackageExecutablePath(NPM_DIR, command)
                        .orElseThrow(() -> new RuntimeException("Could not locate command " + command));
            } else {
                return lookupGlobalNpmPackageExecutablePath(NPM_DIR, command).orElse(null);
            }
        }
    }

    private static Optional<String> lookupGlobalNpmPackageExecutablePath(File directory, String executableName) {
        if (directory != null) {
            File[] children = directory.listFiles();
            if (children != null) {
                for (File child : children) {
                    if (child.isDirectory()) {
                        if (child.getName().equals("bin")) {
                            for (File binFile : child.listFiles()) {
                                if (binFile.getName().equals(executableName)) {
                                    return Optional.of(binFile.getAbsolutePath());
                                }
                            }
                        } else {
                            Optional<String> foundPath = lookupGlobalNpmPackageExecutablePath(child, executableName);
                            if (foundPath.isPresent()) {
                                return foundPath;
                            }
                        }
                    }
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Gets the full path of a local package's JS main file installed with npm.
     *
     * @param projectDirectory
     */
    public static String getLocalNpmPackageExecutablePath(String command, File projectDirectory) {
        File commandFile = new File(command);
        if (commandFile.isFile() && commandFile.isAbsolute()) {
            return command;
        }

        return projectDirectory.getAbsolutePath() + File.separator + "node_modules" + File.separator + ".bin"
                + File.separator + command + (isWindows() ? ".cmd" : "");
    }

    /**
     * Gets the full path of a global package installed with npm.
     */
    public static String getGlobalNpmPackagePath(String packageName) {
        return NPM_DIR.getPath() + File.separator + "node_modules" + File.separator + packageName;
    }

    /**
     * Tells if this node command is globally installed.
     */
    public static boolean isExecutableInstalledGloballyWithNpm(String command) {
        String executablePath = getGlobalNpmPackageExecutablePath(command, false);
        return executablePath != null && new File(executablePath).exists();
    }

    /**
     * Tells if this node command is locally or globally installed
     */
    public static boolean isExecutableInstalledLocallyWithNpm(String command, File projectDirectory) {
        String executablePath = getLocalNpmPackageExecutablePath(command, projectDirectory);
        return executablePath != null && new File(executablePath).exists();
    }

    /**
     * Tells if this node package is installed in global modules
     *
     * @param packageName package name
     */
    public static boolean isPackageInstalledGloballyWithNpm(String packageName) {
        return new File(getGlobalNpmPackagePath(packageName)).exists();
    }

    /**
     * Tells if this node package is locally installed
     */
    public static boolean isPackageInstalledLocallyWithNpm(String packageName, File projectDirectory) {
        return new File(getLocalNpmPackagePath(packageName, projectDirectory)).exists();
    }

    public static String getLocalNpmPackagePath(String packageName, File projectDirectory) {
        return projectDirectory.getAbsolutePath() + File.separator + "node_modules" + File.separator + packageName;
    }

    /**
     * Runs the given command.
     *
     * @param command        the command name
     * @param stdoutConsumer consumes the standard output stream as lines of
     *                       characters
     * @param errorHandler   upcalled when the command does not terminate
     *                       successfully
     * @param args           the command-line arguments
     * @return the process that was created to execute the command (exited at this
     *         point)
     */
    public static Process runCommand(String command, Consumer<String> stdoutConsumer, Runnable errorHandler,
            String... args) {
        return runCommand(command, null, false, stdoutConsumer, null, errorHandler, args);
    }

    /**
     * Runs the given command in an asynchronous manner.
     *
     * @param command        the command name
     * @param stdoutConsumer consumes the standard output stream as lines of
     *                       characters
     * @param endConsumer    called when the process actually ends
     * @param errorHandler   upcalled when the command does not terminate
     *                       successfully
     * @param args           the command-line arguments
     * @return the process that was created to execute the command (can be still
     *         running at this point)
     */
    public static Process runAsyncCommand(String command, Consumer<String> stdoutConsumer,
            Consumer<Process> endConsumer, Runnable errorHandler, String... args) {
        return runCommand(command, null, true, stdoutConsumer, endConsumer, errorHandler, args);
    }

    /**
     * Runs the given command.
     *
     * @param command        the command name
     * @param directory      the working directory of the created process
     * @param async          tells if the command should be run asynchronously (in a
     *                       separate thread)
     * @param stdoutConsumer consumes the standard output stream as lines of
     *                       characters
     * @param endConsumer    called when the process actually ends
     * @param errorHandler   upcalled when the command does not terminate
     *                       successfully
     * @param args           the command-line arguments
     * @return the process that was created to execute the command (can be still
     *         running at this point if <code>async</code> is <code>true</code>)
     */
    public static Process runCommand(String command, File directory, boolean async, Consumer<String> stdoutConsumer,
            Consumer<Process> endConsumer, Runnable errorHandler, String... args) {

        String[] cmd;
        if (isWindows()) {
            String commandExecutableName = FilenameUtils.getBaseName(command);
            if (nodeCommandsBaseNames.contains(commandExecutableName)) {
                cmd = new String[] { getGlobalNpmPackageExecutablePath(command) };
            } else {
                cmd = new String[] { "cmd", "/c", command };
            }
            cmd = ArrayUtils.addAll(cmd, args);
        } else {
            if (nodeCommandsBaseNames.contains(command)) {
                cmd = new String[] { getGlobalNpmPackageExecutablePath(command) };
                cmd = ArrayUtils.addAll(cmd, args);
            } else {
                String cmdAndArgs = StringUtils.join(ArrayUtils.insert(0, args, command), " ");
                cmd = new String[] { "/bin/sh", "-c", cmdAndArgs };
            }
        }
        logger.debug("run command: " + StringUtils.join(cmd, " "));
        Process[] processReference = { null };
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(cmd);
            processBuilder.redirectErrorStream(true);
            if (directory != null) {
                processBuilder.directory(directory);
            }
            if (!StringUtils.isBlank(EXTRA_PATH)) {
                processBuilder.environment().put("PATH",
                        processBuilder.environment().get("PATH") + File.pathSeparator + EXTRA_PATH);
            }

            Process process = processBuilder.start();
            processReference[0] = process;
            logger.debug("started " + processBuilder.command());

            Runtime.getRuntime().addShutdownHook(new Thread(() -> process.destroyForcibly()));

            Runnable runnable = new Runnable() {

                @Override
                public void run() {
                    try {

                        try (BufferedReader in = new BufferedReader(
                                new InputStreamReader(process.getInputStream(), "UTF-8"))) {
                            String line;
                            while ((line = in.readLine()) != null) {
                                if (stdoutConsumer != null) {
                                    stdoutConsumer.accept(line);
                                } else {
                                    logger.info(command + " - " + line);
                                }
                            }
                        }

                        process.waitFor();
                        if (endConsumer != null) {
                            endConsumer.accept(process);
                        }
                        if (process.exitValue() != 0) {
                            if (errorHandler != null) {
                                errorHandler.run();
                            }
                        }
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                        if (errorHandler != null) {
                            errorHandler.run();
                        }
                    }
                }
            };
            if (async) {
                new Thread(runnable).start();
            } else {
                runnable.run();
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            if (errorHandler != null) {
                errorHandler.run();
            }
            return null;
        }
        return processReference[0];
    }

    public static boolean isWindows() {
        return System.getProperty("os.name").startsWith("Windows");
    }

    /**
     * Installs a <code>node<code> package with <code>npm</code> (assumes that
     * <code>node</code> is installed).
     *
     * @param nodePackageName the package name
     */
    public static void installGlobalNodePackage(String nodePackageName, String version) {
        logger.debug("installing " + nodePackageName + " with npm");
        initNode();
        runCommand(NPM_COMMAND, USER_HOME_DIR, false, null, null, null, "install", "--prefix", NPM_DIR.getPath(),
                version == null ? nodePackageName : nodePackageName + "@" + version);
    }

    /**
     * Installs a <code>node<code> package with <code>npm</code> (assumes that
     * <code>node</code> is installed).
     *
     * @param nodePackageName the package name
     */
    public static void installLocalNodePackage(String nodePackageName, String version, File projectDirectory) {
        logger.debug("installing " + nodePackageName + " local with npm");
        initNode();
        checkNpmProjectInitialization(projectDirectory);
        runCommand(NPM_COMMAND, projectDirectory, false, null, null, null, "install",
                version == null ? nodePackageName : nodePackageName + "@" + version, "--save");
    }

    private static void checkNpmProjectInitialization(File projectDirectory) {
        if (!new File(projectDirectory, "package.json").isFile()) {
            runCommand(NPM_COMMAND, projectDirectory, false, null, null, null, "init", "-y");
        }
    }

    /**
     * Checks if a node package has been installed locally.
     *
     * @param nodePackageName the node module to be tested
     * @return true if already installed locally
     */
    public static boolean isNodePackageInstalled(String nodePackageName) {
        logger.debug("checking installation of " + nodePackageName + " with npm");
        initNode();
        boolean[] installed = { false };
        runCommand(NPM_COMMAND, USER_HOME_DIR, false, line -> {
            if (!installed[0]) {
                installed[0] = line.endsWith("/" + nodePackageName);
            }
        }, null, null, "ls", "--parseable", nodePackageName);
        return installed[0];
    }

    /**
     * Uninstalls a <code>node<code> package with <code>npm</code> (assumes that
     * <code>node</code> is installed).
     *
     * @param nodePackageName the package name
     * @param location        <code>Global</code> for adding the <code>-g</code>
     *                        option
     */
    public static void uninstallGlobalNodePackage(String nodePackageName) {
        logger.debug("uninstalling " + nodePackageName + " with npm");
        initNode();
        runCommand(NPM_COMMAND, USER_HOME_DIR, false, null, null, null, "uninstall", "--prefix", NPM_DIR.getPath(),
                nodePackageName, "-g");
    }

    public static boolean isVersionHighEnough(String currentVersion, String minimumVersion) {

        currentVersion = currentVersion.replace("v", "");
        minimumVersion = minimumVersion.replace("v", "");

        String[] currentVersionParts = currentVersion.split("[.]");
        String[] minimumVersionParts = minimumVersion.split("[.]");

        for (int i = 0; i < currentVersionParts.length; i++) {
            if (minimumVersionParts.length <= i) {
                break;
            }

            try {
                int currentVersionPart = Integer.parseInt(currentVersionParts[i]);
                int minimumVersionPart = Integer.parseInt(minimumVersionParts[i]);
                if (currentVersionPart > minimumVersionPart) {
                    return true;
                } else if (currentVersionPart < minimumVersionPart) {
                    return false;
                }
            } catch (NumberFormatException e) {
                logger.error("unexpected version token " + currentVersion + " / " + minimumVersion, e);
            }
        }

        return true;
    }

    private static final Map<String, String> globalExecutableCache = new HashMap<>();

    public static String findGlobalExecutable(String fileName, String packageName) {

        String path = globalExecutableCache.get(fileName);
        if (path == null) {
            String globalPackagesDir = NPM_DIR.getAbsolutePath();

            Stream<Path> searchPaths = null;
            try {

                searchPaths = Files.walk(Paths.get(globalPackagesDir));
                if (isWindows()) {
                    File programFilesPath = new File(
                            System.getenv("ProgramFiles") + "/nodejs/node_modules/" + packageName);
                    if (programFilesPath.isDirectory()) {
                        searchPaths = Stream.concat(searchPaths, Files
                                .walk(Paths.get(programFilesPath.getAbsolutePath()), FileVisitOption.FOLLOW_LINKS));
                    }
                }

                path = searchPaths //
                        .filter(Files::isRegularFile)//
                        .filter((f) -> {
                            String file = f.toFile().getName();
                            return file.equals(fileName);
                        }) //
                        .map(f -> f.toFile().getAbsolutePath()).findFirst() //
                        .orElse(null);

                globalExecutableCache.put(fileName, path);
            } catch (Exception e) {
                throw new RuntimeException("cannot find global executable", e);
            } finally {
                if (searchPaths != null) {
                    searchPaths.close();
                }
            }
        }
        return path;
    }

}
