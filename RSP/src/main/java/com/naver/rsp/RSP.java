package com.naver.rsp;

import lombok.extern.slf4j.Slf4j;
import nu.pattern.OpenCV;
import org.apache.commons.io.FilenameUtils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class RSP {

    private static final ResourcePathFinder FINDER = new ResourcePathFinder();

    private static CascadeClassifier FACE_DETECTOR;

    private static final String CASCADE_FILE_PATH = "lbpcascades/lbpcascade_frontalface.xml";
    private static final String ROCK_SCISSORS_PAPER = "assets/rock_scissors_paper.png";
    private static final String ROCK_SCISSORS_PAPER_MASK = "assets/rock_scissors_paper_mask.png";

    private static Mat sticker;
    private static Mat sticker_mask;

    static {
        init();
    }

    private static void init() {
        OpenCV.loadShared();
        log.debug("[OpenCV Loaded]");

        FACE_DETECTOR = new CascadeClassifier(FINDER.getClassPathResource(CASCADE_FILE_PATH));

        sticker = Imgcodecs.imread(FINDER.getClassPathResource(ROCK_SCISSORS_PAPER));
        log.debug("가위바위보 이미지 로드");

        sticker_mask = Imgcodecs.imread(FINDER.getClassPathResource(ROCK_SCISSORS_PAPER_MASK));
        log.debug("가위바위보 마스크 이미지 로드");

        log.debug("[[[ RSP is Ready!! ]]]\n\n\n");
    }

    public static Path run(String sourcePath) {
        String destinationPath = FilenameUtils.getFullPath(sourcePath) + FilenameUtils.getBaseName(sourcePath) + "_result." + FilenameUtils.getExtension(sourcePath);
        log.debug("source: {}, destination: {}", sourcePath, destinationPath);

        writeImage(sourcePath, destinationPath);

        Path path = Paths.get(destinationPath);
        log.debug("{}", "======================================PROCESS END======================================");
        return path;
    }

    private static void writeImage(String inputFilename, String outputFilename) {
        Mat img = Imgcodecs.imread(inputFilename);

        MatOfRect faceDetections = new MatOfRect();
        FACE_DETECTOR.detectMultiScale(img, faceDetections);
        log.debug("The number of face: {}", faceDetections.size());

        for (Rect rect : faceDetections.toArray()) {
            //Imgproc.rectangle(img, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 0, 255), 3);
            int rspIdx = (int) Math.min(Math.random() * 3, 2);
            attachSticker(1, 3, 0, rspIdx, rect, img);
        }

        Imgcodecs.imwrite(outputFilename, img);
    }

    // TODO Have to replace GL code
    private static void attachSticker(int rows, int cols, int row, int col, Rect rect, Mat img) {
        int sw = sticker.cols() / cols;
        int sh = sticker.rows() / rows;

        Rect stickerRoi = new Rect();
        stickerRoi.width = sw;
        stickerRoi.height = sh;
        stickerRoi.x = col * sw;
        stickerRoi.y = row * sh;

        Mat croppedSticker = sticker.submat(stickerRoi);
        Mat scaledSticker = new Mat();
        Imgproc.resize(croppedSticker, scaledSticker, new Size(rect.width, rect.height));

        Mat croppedMask = sticker_mask.submat(stickerRoi);
        Mat scaledMask = new Mat();
        Imgproc.resize(croppedMask, scaledMask, new Size(rect.width, rect.height));

        Mat dst = img.submat(rect);

        log.debug("사이즈: " + dst.cols() + "x" + dst.rows() + "-" + scaledSticker.cols() + "x" + scaledSticker.rows());
        log.debug("타입: " + img.type() + " - " + scaledSticker.type());

        scaledSticker.copyTo(dst, scaledMask);

        croppedSticker.release();
        scaledSticker.release();
        croppedMask.release();
        scaledMask.release();
    }

}
