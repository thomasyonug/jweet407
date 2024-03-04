import typescript from "@rollup/plugin-typescript";

export default [
    {
        input: ['src/WebWorker.ts', 'src/ChannelCenter.ts'],
        output: {
            dir: 'dist',
        },
        plugins: [
            typescript( { target: 'esnext' })
        ],
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
    }
]