import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class CapchaDownloader {

    private static final String adress = "https://ems.ms.gov.pl/krs/wyszukiwaniepodmiotu?t:lb=t";
    private static final String outputFolder = "C:/capcha/";

    public void getCapcha(int downloadCount) throws IOException
    {
        for (int i = 0;i<downloadCount;i++)
        {
            Document document =  Jsoup.connect(adress).get();
            Element captchaIMG = document.getElementById("kaptchaZone");
            String number = captchaIMG.toString().substring(captchaIMG.toString().indexOf(':')+7,captchaIMG.toString().indexOf(" /> ")-1);
            downloadCapchaImage(parseCapchaImageURL(number),number);
        }
    }

    private void createFile(String path)
    {
        File file = new File(path);
        if (file.exists())
        {
            return;
        }
        else
        {
            try {
                file.createNewFile();
            } catch (IOException e)
            {
                System.out.println("Ошибка создания файла.");
            }
        }
    }

    private void writeToFile(InputStream stream,String path)
    {
        try
        {
            createFile(path);
            OutputStream writer = new FileOutputStream(path);
            byte buffer[] = new byte[65535];
            int c = stream.read(buffer);
            while (c > 0) {
                writer.write(buffer, 0, c);
                c = stream.read(buffer);
            }
            writer.flush();
            writer.close();
            stream.close();
        } catch (Exception e)
        {
            System.out.println("Ошибка записи в файл(файл не существует, или заблокирован).");
        }
    }

    private String parseCapchaImageURL(String number)
    {
       return "https://ems.ms.gov.pl/krs/wyszukiwaniepodmiotu.kaptchacomponent.kaptcha:image/".concat(number);
    }

    private void downloadCapchaImage(String imageURL,String capchaNumber)
    {
        try {
            URL url = new URL(imageURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            InputStream stream = connection.getInputStream();
            writeToFile(stream,outputFolder.concat(capchaNumber).concat(".jpg"));
        }
        catch (Exception e)
        {
            System.out.println("Wrong download CAPCHA image.");
        }
    }
}
