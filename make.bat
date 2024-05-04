xas99.py -b png\title-screen-top.a99 -o src\title-screen-top.bin
xas99.py -b png\title-screen-bottom.a99 -o src\title-screen-bottom.bin

xas99.py -R -S -L karts.lst -i -q -18 -o bin/KARTS src/karts.a99
java -jar tools/ea5tocart.jar bin\KARTS "F18A KARTS DEMO" > make.log

copy /b .\bin\KARTS8.bin + ^
    .\bin\empty512.bin + ^
    .\bin\title-screen-top.bin + ^
    .\bin\empty512.bin + ^
    .\bin\title-screen-bottom.bin + ^
    .\bin\empty.bin + ^
    .\bin\empty.bin + ^
    .\karts8.bin

java -jar tools/CopyHeader.jar karts8.bin 60
