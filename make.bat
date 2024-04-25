xas99.py -R -S -L karts.lst -i -q -18 -o bin/KARTS src/karts.a99
java -jar tools/ea5tocart.jar bin\KARTS "F18A KARTS DEMO" > make.log
copy .\bin\KARTS8.bin .\karts8.bin
