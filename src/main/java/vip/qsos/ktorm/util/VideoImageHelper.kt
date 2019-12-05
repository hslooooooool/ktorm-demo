package vip.qsos.ktorm.util

import org.bytedeco.javacpp.opencv_core
import org.bytedeco.javacpp.opencv_core.IplImage
import org.bytedeco.javacv.FFmpegFrameGrabber
import org.bytedeco.javacv.Frame
import org.bytedeco.javacv.Java2DFrameConverter
import org.bytedeco.javacv.OpenCVFrameConverter.ToIplImage
import java.io.File
import java.io.IOException
import java.util.*
import javax.imageio.ImageIO

/**
 * @author : 华清松
 * 视频封面处理
 */
object VideoImageHelper {

    private const val IMAGE_MAT = "png"
    private const val ROTATE = "rotate"
    /**
     * 默认截取视频的中间帧为封面
     */
    const val MOD = 2

    /**
     * 获取视频缩略图
     *
     * @param filePath：视频路径
     * @param mod：视频长度/mod获取第几帧
     */
    @Throws(Exception::class)
    fun randomGrabberFFmpegImage(filePath: String, mod: Int): String? {
        var targetFilePath = ""
        val ff = FFmpegFrameGrabber.createDefault(filePath)
        ff.start()
        val rotate = ff.getVideoMetadata(ROTATE)
        val ffLength = ff.lengthInFrames
        var f: Frame?
        var i = 0
        val index = ffLength / mod
        while (i < ffLength) {
            f = ff.grabImage()
            if (i == index) {
                if (null != rotate && rotate.length > 1) {
                    val converter = ToIplImage()
                    val src = converter.convert(f)
                    f = converter.convert(rotate(src, rotate.toInt()))
                }
                targetFilePath = getImagePath(filePath, i)
                doExecuteFrame(f, targetFilePath)
                break
            }
            i++
        }
        ff.stop()
        return targetFilePath
    }

    /**
     * 根据视频路径生成缩略图存放路径
     *
     * @param filePath：视频路径
     * @param index：第几帧
     * @return 缩略图的存放路径
     */
    private fun getImagePath(filePath: String, index: Int): String {
        var path = filePath
        if (path.contains(".") && path.lastIndexOf(".") < path.length - 1) {
            path = path.substring(0, path.lastIndexOf(".")) + "_" + index.toString() + "." + IMAGE_MAT
        }
        return path
    }

    /**
     * 旋转图片
     * @param src
     * @param angle
     */
    private fun rotate(src: IplImage, angle: Int): IplImage {
        val img = IplImage.create(src.height(), src.width(), src.depth(), src.nChannels())
        opencv_core.cvTranspose(src, img)
        opencv_core.cvFlip(img, img, angle)
        return img
    }

    /**
     * 截取缩略图
     *
     * @param f              帧
     * @param targetFilePath 封面图片
     */
    private fun doExecuteFrame(f: Frame?, targetFilePath: String?) {
        if (f?.image == null || targetFilePath == null) {
            return
        }
        val converter = Java2DFrameConverter()
        val bi = converter.getBufferedImage(f)
        val output = File(targetFilePath)
        try {
            ImageIO.write(bi, IMAGE_MAT, output)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /**
     * 根据视频长度随机生成随机数集合
     *
     * @param baseNum:基础数字，此处为视频长度
     * @param length：随机数集合长度
     * @return 随机数集合
     */
    fun random(baseNum: Int, length: Int): List<Int> {
        val list: MutableList<Int> = ArrayList(length)
        while (list.size < length) {
            val next = (Math.random() * baseNum).toInt()
            if (list.contains(next)) {
                continue
            }
            list.add(next)
        }
        Collections.sort(list)
        return list
    }
}