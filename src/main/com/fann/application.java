import java.io.IOException;
public class application {
    public static void main(String[] args) throws IOException
    {
        CapchaDownloader downloader = new CapchaDownloader();

        downloader.getCapcha(1);

    }

}
