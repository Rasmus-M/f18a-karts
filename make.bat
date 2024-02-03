xas99.py -R -S -L mode7.lst -i -q -18 -o bin/MODE7 src/mode7.a99
java -jar tools/ea5tocart.jar bin\MODE7 "MODE 7 DEMO"
copy .\bin\MODE78.bin .\mode7-8.bin
