# F18A Karts

A few months ago there was a bit of discussion about games similar to Mario Kart:
https://forums.atariage.com/topic/359151-another-youtube-video/?do=findComment&comment=5382710
This made me wonder what we could do on the TI.

TheMole linked to a demo on the MSX1 using the TMS9918A VDP, but I soon came to the conclusion that in order to produce something playable on the TI we needed help from the F18A.

The F18A supports two types of bitmaps that would be suitable for something like this: a 4 color bitmap with 256 horizontal pixels and a 16 color bitmap with 128 'fat' pixels. I decided on the latter in order to get a more colorful display.

Mario Kart used the ability of the SNES hardware to scale and rotate a 2D image to make it look like 3D. The source images could be as big as 1024x1024 pixels. I thought the F18A GPU would be fast enough to do something similar, but where would we store the source image? The F18A only has 18K RAM, and a 1024x1024 bitmap would take 512K! And it takes 12K to display a bitmap that covers the whole screen on the F18A, so after displaying the bitmap there would only be 6K left for the source image and everything else, like sprites and the GPU program.

My first approach was to build the source image from 8x8 meta-tiles, which again consisted of 8x8 pixel tiles (64 x 64 pixels in total). The meta-tile map for a 1024x1024 image would then only take 256 bytes, plus 1024 bytes to store 16 meta-tile, plus the space to store the tiles they consisted of. However, my attempts to use this approach turned out to be way too slow (drawing an image tokk several seconds).

For my next approach I looked at Mario Kart, which has a 3D image at the top and an overview image at the bottom (also 3D but seen more from above). Maybe we could have a 2D overview image at the bottom of the screen and use that as the source image for the 3D image at the top of the screen? The source image would have a much smaller resolution than 1024x1024 (actually 256x128 pixels) so the 3D result would also be much more pixelated. But the transformation from one bitmap to another could be much faster than with the meta-tiles.

And it turned out to work fine, much faster than I would have thought, actually more than 60 FPS when generating a 256x64 pixels 3D image. It took a lot of time to understand how to make a proper 3D perspective transformation without any distortion such as fish-eye effects.

The next step was to look at the scrolling background.

And the the sprites...




