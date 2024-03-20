class Logger {
    static debug(message) {
        console.debug(`[DEBUG] ${new Date().toISOString()}: ${message}`);
    }
    static info(message) {
        console.log(`[INFO] ${new Date().toISOString()}: ${message}`);
    }
    static warn(message) {
        console.warn(`[WARN] ${new Date().toISOString()}: ${message}`);
    }
    static error(message) {
        console.error(`[ERROR] ${new Date().toISOString()}: ${message}`);
    }
}

module.exports = Logger;