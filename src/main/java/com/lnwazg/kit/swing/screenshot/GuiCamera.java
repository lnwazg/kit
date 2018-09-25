package com.lnwazg.kit.swing.screenshot;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

/**
 * 屏幕截图工具类
 * @author nan.li
 * @version 2016年12月29日
 */
public class GuiCamera
{
    //文件目录名称
    private String fileDirName;
    
    //图像文件的格式
    private String imageFormat;
    
    private String DefaultName = "GuiCamera";
    
    private String DefaultImageFormat = "png";
    
    private int x = 0;
    
    private int y = 0;
    
    private int width = (int)ScreenKit.screenWidth;
    
    private int height = (int)ScreenKit.screenHeight;
    
    public GuiCamera()
    {
        this.fileDirName = DefaultName;
        this.imageFormat = DefaultImageFormat;
    }
    
    public GuiCamera(String fileDirName, String imageFormat)
    {
        this.fileDirName = fileDirName;
        this.imageFormat = imageFormat;
    }
    
    public GuiCamera(String fileDirName, String imageFormat, int x, int y, int width, int height)
    {
        this.fileDirName = fileDirName;
        this.imageFormat = imageFormat;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    
    /**
     * 屏幕快照，并返回生成的图片的路径
     * @author nan.li
     * @return
     */
    public String snapShot()
    {
        return snapShot(null);
    }
    
    /**
     * 对屏幕进行拍照
     * @author nan.li
     */
    public String snapShot(Integer no)
    {
        try
        {
            Robot robot = new Robot();
            //唤醒屏幕
            //robot.keyPress(KeyEvent.VK_SPACE);
            // 拷贝屏幕到一个BufferedImage对象screenshot
            BufferedImage screenshot = (robot).createScreenCapture(new Rectangle(x, y, width, height));
            // 根据文件前缀变量和文件格式变量，自动生成文件名
            String fileFullPath = null;
            if (no != null)
            {
                fileFullPath = fileDirName + String.valueOf(no) + "." + imageFormat;
            }
            else
            {
                fileFullPath = fileDirName + "." + imageFormat;
            }
            
            File file = new File(fileFullPath);
            if (!file.exists())
            {
                file.getParentFile().mkdirs();
            }
            System.out.print("Save File " + fileFullPath + " ... ");
            // 将screenshot对象写入图像文件
            ImageIO.write(screenshot, imageFormat, file);
            System.out.println("Finished!");
            return fileFullPath;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 连续截图10张
     * @author nan.li
     */
    public String[] snapShot10Photos(int intervalMillseconds)
    {
        return snapShotSeveral(10, intervalMillseconds);
    }
    
    /**
     * 连续截图若干张
     * @author nan.li
     * @param num
     * @param intervalMillseconds  间隔的毫秒数
     */
    public String[] snapShotSeveral(int num, int intervalMillseconds)
    {
        //        List<String> ret = new ArrayList<>();
        String[] ret = new String[num];
        for (int i = 0; i < num; i++)
        {
            //            ret.add(snapShot(i));
            ret[i] = snapShot(i);
            try
            {
                TimeUnit.MILLISECONDS.sleep(intervalMillseconds);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        return ret;
    }
    
    public static void main(String[] args)
    {
        GuiCamera guiCamera = new GuiCamera("d:\\secure\\sc", "png");
        //        System.out.println(guiCamera.snapShot10Photos(20));
        System.out.println(guiCamera.snapShotSeveral(10, 20));
        
        //        GuiCamera cam = new GuiCamera("d:\\sc", "png");
        //        GuiCamera cam = new GuiCamera("d:\\sc", "png", 0, 0, 1920 / 2, 1080 / 2);
        //        GuiCamera cam = new GuiCamera("d:\\sc", "png", ScreenKit.screenWidthHalf, ScreenKit.screenHeightHalf, ScreenKit.screenWidthHalf, ScreenKit.screenHeightHalf);
        //        GuiCamera cam = new GuiCamera("d:\\sc", "png", ScreenKit.screenWidthHalfOneThird, ScreenKit.screenHeightHalfOneThird, ScreenKit.screenWidthHalfOneThird * 4, ScreenKit.screenHeightHalfOneThird * 4);
        //        cam.snapShot10Photos(20);
    }
}