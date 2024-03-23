import typescript from "@rollup/plugin-typescript";
import copy from "rollup-plugin-copy"

export default [
    {
        input: ['src/WebWorker.ts', 'src/ChannelRegistry.ts', 'src/initWorker.ts', 'src/Logger.ts'],
        output: {
            dir: 'dist',
        },
        plugins: [
            typescript( { target: 'esnext' }),
            copy({
                targets: [
                    { src: 'dist/initWorker.js', dest: 'test'},
                    { src: 'dist/Logger.js', dest: 'test'}
                ],
                hook: 'writeBundle'
            })
        ],
        treeshake: false, // Disables tree shaking
    },
    {
        input: 'test/Cook.ts',
        output: {
            file: 'test/Cook.js'
        },
        plugins: [
            // nodeResolve(),
            typescript( { target: 'esnext' })
        ],
        treeshake: false, // Disables tree shaking
    }
]