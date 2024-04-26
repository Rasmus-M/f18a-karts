# F18A Karts

In january 2024 there was a bit of discussion about games similar to Mario Kart:
https://forums.atariage.com/topic/359151-another-youtube-video/?do=findComment&comment=5382710
This made me wonder what we could do on the TI.

@TheMole linked to a demo on the MSX1 using the TMS9918A VDP, but I soon came to the conclusion that in order to produce something playable on the TI we needed help from the F18A.

The F18A supports two types of bitmap layers that would be suitable for something like this: a 4 color bitmap with up to 256 horizontal pixels and a 16 color bitmap with up to 128 'fat' pixels. I decided on the latter in order to get a more colorful display.

## 3D view

Mario Kart uses the ability of the SNES hardware to scale and rotate a 2D image to make it look like 3D (aka. Mode 7). The source images could be as big as 1024x1024 pixels. I thought the F18A GPU would be fast enough to do something similar, but where would we store the source image? The F18A only has 18K RAM, and a 1024x1024 bitmap would take 512K! And it takes 12K just to display a bitmap that covers the whole screen on the F18A, so after displaying the bitmap there would only be 6K left for the source image and everything else, like sprites and the GPU program.

My first approach was to build the source image from 8x8 meta-tiles, which again consisted of standard 8x8 pixel characters/tiles (64 x 64 pixels in total). The meta-tile map for a 1024x1024 image would then only take 256 bytes, plus 1024 bytes to store 16 meta-tiles, plus the space to store the tiles they consisted of. However, my attempts to use this approach turned out to be way too slow (drawing an image took several seconds).

For my next approach I looked at Mario Kart, which has a 3D image at the top and an overview image at the bottom (also 3D but seen more from above). Maybe I could have a 2D overview image at the bottom of the screen and use that as the source image for the 3D image at the top of the screen? The source image would have a much smaller resolution than 1024x1024 (actually 128x128 fat pixels) so the 3D result would also be much more pixelated. But the transformation from one bitmap to another could be done much faster than the attempt to use meta-tiles.

And it turned out to work even faster than I would have thought, actually more than 60 FPS when generating a 128x64 pixels 3D image. It took a lot of time to figure out how to make a proper 3D perspective transformation without any distortion such as fish-eye effects, but I'm not going into details about that here. The resultions wasn't too bad either, although nowhere nearly as good as Mario Kart. At this point I had used 12K VDP RAM, plus some more for GPU code in the upper 2K RAM.

## Background

I also wanted a horizontally scrolling background (trees, mountains) at the top of the 3D screen like in Mario Kart. I decided to do that using the normal tile mode and the hardware scrolling of the F18A rather than the bitmap layer in order to speed things up and perhaps save some VDP RAM. I had already used 192 vertical lines (64 lines for the 3D image and 128 lines for the source/overview image), but here the F18A ROW30 mode, which expands the vertical resolution to 240 pixels, came to my rescue, so the top 48 lines could be used for the background and to display other information like time and position. First I added a single layer with mountains, which took little VDP RAM since I only needed 16 characters/tiles plus 6 rows of the name table to implement this. Then I tried adding another layer using F18A TL2 with trees that scrolls at another speed, and I liked it so much I couldn't bring myself to remove it again. Unfortunately that took up much more VDP RAM since TL2 cannot be displayed below the bitmap layer, so I needed space for an additional, full name table. All that used about 1.6K VDP RAM, so now I only had about 2.4K left. Still better than using the bitmap layer for background, which would have required about 2 times the RAM.

## Sprites

The last part of the graphics was to look at how to do the sprites for the player's karts, other karts, and other objects on the track. At first, I thought I could use hardware sprites for everything, but a single sprite pattern in 32x16 pixels 4 colors takes 128 bytes, and for any kind of reasonable 3D scaling effect I would need something like 16 patterns per angle per sprite. Already one sprite would take up the VDP RAM I had left, so I decided only to use hardware sprites for the player's kart, which consists of two magnified 16x16 sprites in 4 colors. The other sprites would have to be scaled and drawn on the 3D bitmap by the GPU. I could foresee two problems with that: firstly the GPU might not be fast enough to also draw the sprites, and secondly, since there was no VDP RAM left for double buffering, would the sprites flicker horrible when the 3D image and the sprites were repeatedly drawn on top of each other? Back in the days scanline renderers, where everything was drawn one scanline at a time, were sometimes used, but I didn't want to go into that kind of trouble yet. And again, it turned out not to be a bad as I feared. Even though you see some flickering, it's not, for instance, hiding important details to the player. But how I wish I had some more VDP RAM to do some proper double buffering...

The hardware sprites for the player's kart took 256 bytes for the patterns plus 128 bytes for sprite attributes, now there was only about 2K VDP RAM left. The current demo only includes 6 different software sprite patterns: 4 patterns for the other karts seen from different angles, one for the green oil drum, and one for the stack of tires. Together they take up about 1K, so there is still a bit of VDP RAM left to expand the demo, but not enough, for instance, to make different patterns for each kart.

I also used hardware sprite for the top display of time, position, and laps, and for the small karts at the bottom of the screen. Interestingly, the F18A allows you to choose whether each sprite is 8x8 or 16x16 pixels, but the magnification setting is the same for all sprites, so the bottom sprites are magnified 8x8 sprites with very few pixels.

## What the TMS9900 is doing

The F18A is not doing all the work, there is plenty left for the TMS9900:
- Reading the joystick
- Moving the player on the track
- Updating attributes for hardware sprites
- Checking that you stay on track
- Moving the other karts
- "Uploading" other kart data to the VDP 
- Playing sound and music
- Speech

For the player's kart movement I asked in the forum (https://forums.atariage.com/topic/362756-physics-model-for-car/#comment-5425008) and @sometimes99er suggested this https://github.com/pakastin/car, which I adopted. All numbers are stored as 8.8 fixed point numbers, where the most significant byte contains the integer part.

To move the other karts, I created a low resolution version of the map, where each byte value determines the direction at that position and whether it's inside the track. In addition to that, each other kart has a base speed and a setting for how much it drifts. This is enough to move the karts around the track for the demo, but hardly enough to make them interesting opponents. So a lot more work would be required to change this from a graphics demo into an exciting game.

