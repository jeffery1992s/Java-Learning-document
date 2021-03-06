package com.easycoding.admin.util;

import javazoom.spi.mpeg.sampled.file.MpegAudioFileReader;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
/**
 * @author chunming.jiang
 * @Description //TODO $
 * @date $ 2019/11/22$
 **/
public class AudioUtils {
    public static void main(String [] args){
        AudioUtils.convertMP3ToPcm();
    }
    /**
     * MP3转换PCM
     *
     * @param mp3filepath 原始文件路径
     * @param pcmfilepath 转换文件的保存路径
     * @return
     * @throws Exception
     */
    public static boolean convertMP3ToPcm() {
        //String mp3filepath, String pcmfilepath
        try {
            // 获取文件的音频流，pcm的格式
            AudioInputStream audioInputStream = getPcmAudioInputStream("src/main/resources/file/城市便捷（新版彩铃2019）(1).mp3");
            // 将音频转化为 pcm的格式保存下来
            AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, new File("src/main/resources/file/城市便捷（新版彩铃2019）(1).pcm"));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 播放MP3
     *
     * @param mp3filepath
     * @throws Exception
     */
    public static void playMP3(String mp3filepath) throws Exception {
        // 获取音频为pcm的格式
        AudioInputStream audioInputStream = getPcmAudioInputStream(mp3filepath);
        // 播放
        if (audioInputStream == null) {
            System.out.println("null audiostream");
            return;
        }
        // 获取音频的格式
        AudioFormat targetFormat = audioInputStream.getFormat();
        DataLine.Info dinfo = new DataLine.Info(SourceDataLine.class, targetFormat, AudioSystem.NOT_SPECIFIED);
        // 输出设备
        SourceDataLine line = null;
        try {
            line = (SourceDataLine) AudioSystem.getLine(dinfo);
            line.open(targetFormat);
            line.start();
            int len = -1;
            byte[] buffer = new byte[1024];
            // 读取音频文件
            while ((len = audioInputStream.read(buffer)) > 0) {
                // 输出音频文件
                line.write(buffer, 0, len);
            }
            // Block等待临时数据被输出为空
            line.drain();
            // 关闭读取流
            audioInputStream.close();
            // 停止播放
            line.stop();

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("audio problem " + ex);
        }finally{
            if(line!=null){
                line.close();
            }
        }
    }
    /**
     * 获取文件的音频流
     *
     * @param mp3filepath
     * @return
     */
    private static AudioInputStream getPcmAudioInputStream(String mp3filepath) {
        File mp3 = new File(mp3filepath);
        AudioInputStream audioInputStream = null;
        AudioFormat targetFormat = null;
        AudioInputStream in = null;
        try {
            // 读取音频文件的类
            MpegAudioFileReader mp = new MpegAudioFileReader();
            in = mp.getAudioInputStream(mp3);
            AudioFormat baseFormat = in.getFormat();
            // 设定输出格式为pcm格式的音频文件
            targetFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16,
                    baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
            // 输出到音频
            audioInputStream = AudioSystem.getAudioInputStream(targetFormat, in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return audioInputStream;
    }
}

