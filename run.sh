
rm tsout/*

java -jar artifacts/jsweet-transpiler-4.0.0-SNAPSHOT-jar-with-dependencies-2024-4-13.jar -i ./example -o ./output --tsout tsout > /dev/null
tsc tsout/*.ts --target es6 > /dev/null

cat tsout/*.js > ./runtime/src/compiled.js

