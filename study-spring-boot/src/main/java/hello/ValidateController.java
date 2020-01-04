package hello;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

@Controller
@RequestMapping("/validate")
public class ValidateController {

    @GetMapping("/refresh")
    public void insert(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setHeader("Content-type", "image/jpeg; charset=UTF-8");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", -1);
        OutputStream outputStream = response.getOutputStream();
        BufferedImage image = new BufferedImage(83, 35, BufferedImage.TYPE_INT_BGR);
        Graphics g = image.getGraphics();
        Color color = g.getColor();
        g.fillRect(0, 0, 83, 35);
        char[] ch = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890".toCharArray();
        int length = ch.length;
        Random random = new Random();
        Font[] font = new Font[5];
        font[0] = new Font("Ravie", Font.PLAIN, 24);    //24为字体粗细
        font[1] = new Font("Autique Olive Compact", Font.PLAIN, 24);
        font[2] = new Font("Forte", Font.PLAIN, 24);
        font[3] = new Font("Wide Latin", Font.PLAIN, 24);
        font[4] = new Font("Gill Sans Ultra Bold", Font.PLAIN, 24);
        Font nowFont = font[random.nextInt(5)];
        for (int i = 0; i < 4; i++) {
            //设置字体的样式
            g.setFont(nowFont);
            String rand = new Character(ch[random.nextInt(length)]).toString();
            g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
            g.drawString(rand, 20 * i + 6, 25);
        }
        //干扰线
        for (int i = 0; i < 100; i++)    //100为干扰线的数量
        {
            int x1 = random.nextInt(83);
            int y1 = random.nextInt(35);
            g.drawOval(x1, y1, 1, 3);    //1,3为干扰线的长宽
        }
        g.setColor(color);
        g.dispose();
        ImageIO.write(image, "JPEG", outputStream);
    }
}
