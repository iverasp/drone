## Compile FFmpeg on the Raspberry Pi

Compile and install x264

    git clone git://git.videolan.org/x264
    cd x264
    ./configure --host=arm-unknown-linux-gnueabi --enable-static --disable-opencl
    make -j4
    make install

Compile and install ffmpeg

    git clone git://source.ffmpeg.org/ffmpeg.git
    cd ffmpeg
    ./configure --arch=armel --target-os=linux --enable-gpl --enable-omx --enable-omx-rpi --enable-nonfree
    make -j4
    make install
