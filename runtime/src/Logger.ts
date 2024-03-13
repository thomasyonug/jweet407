export class Logger {
    static debug(message: string) {
        console.debug(`[DEBUG] ${new Date().toISOString()}: ${message}`);
    }

    static info(message: string) {
        console.log(`[INFO] ${new Date().toISOString()}: ${message}`);
    }

    static warn(message: string) {
        console.warn(`[WARN] ${new Date().toISOString()}: ${message}`);
    }

    static error(message: string) {
        console.error(`[ERROR] ${new Date().toISOString()}: ${message}`);
    }
}