import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import static java.util.concurrent.TimeUnit.NANOSECONDS;


public class JuliaSet {

    public static void main(String[] args)
    {
        // Settings
        int width = 1920;
        int height = 1080;
        int maxIterations = 50000;
        double cRE = -0.726895347709114071439; // C's real component
        double cIM = 0.188887129043845954792; // C's imaginary component
        float hue = 300 / 360f;
        float saturation = 0;
        float brightnessShift = 256;

        // Proceed with caution
        long start = System.nanoTime();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        int[] colors = new int[maxIterations];
        for(int idx = 0; idx < maxIterations; idx++)
            colors[idx] = Color.HSBtoRGB(hue, saturation, 1 - idx / (idx + brightnessShift));

        for(int xPos = 0; xPos < width; xPos++)
        {
            for (int yPos = 0; yPos < height; yPos++)
            {
                double zr = (xPos - width / 2.0) * 5 / width;
                double zi = (yPos - height / 2.0) * -5 / width;
                int iteration = 0;
                while (zr * zr + zi * zi <= 4 && iteration < maxIterations)
                {
                    double newZr = zr * zr - zi * zi + cRE;
                    zi = 2 * zr * zi + cIM;
                    zr = newZr;
                    iteration++;
                }
                if (iteration < maxIterations)
                    image.setRGB(xPos, yPos, colors[iteration]);
                else
                    image.setRGB(xPos, yPos, Color.WHITE.getRGB());
            }
            System.out.println("Percentage done: " + Math.round((100 * (xPos + 1.0) / width ) * 100 ) / 100.0 + "%");
        }
        System.out.println("Finished in " + NANOSECONDS.toSeconds(System.nanoTime() - start) + "s");

        try
        {
            File fileToSavePicture = new File("JuliaSet.png");
            fileToSavePicture.createNewFile();
            ImageIO.write(image, "png", fileToSavePicture);
            System.out.println("Saved to " + fileToSavePicture.getAbsolutePath());
        }
        catch(Throwable e)
        {
            e.printStackTrace();
        }
    }
}
