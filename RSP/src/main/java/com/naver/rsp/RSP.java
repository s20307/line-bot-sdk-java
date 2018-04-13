package com.naver.rsp;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import nu.pattern.OpenCV;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;

@Slf4j
public class RSP {

    private static final ResourcePathFinder FINDER = new ResourcePathFinder();

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

        sticker = Imgcodecs.imread(FINDER.getClassPathResource(ROCK_SCISSORS_PAPER));
        log.debug("가위바위보 이미지 로드");

        sticker_mask = Imgcodecs.imread(FINDER.getClassPathResource(ROCK_SCISSORS_PAPER_MASK));
        log.debug("가위바위보 마스크 이미지 로드");

        log.debug("[[[ RSP is Ready!! ]]]\n\n\n");
    }

    public static File run(String sourcePath) {
        FileInfo fileInfo = new FileInfo(sourcePath);
        log.debug("source: {}, destination: {}", fileInfo.getSourcePath(), fileInfo.getDestinationPath());

        writeImage(fileInfo.getSourcePath(), fileInfo.getDestinationPath());

        File file = new File(fileInfo.getDestinationPath());
        log.debug("{}", "======================================PROCESS END======================================");
        return file;
    }

    private static void writeImage(String inputFilename, String outputFilename) {
        Mat img = Imgcodecs.imread(inputFilename);

        CascadeClassifier faceDetector = new CascadeClassifier();
        if (!faceDetector.load(FINDER.getClassPathResource(CASCADE_FILE_PATH))) {
            log.error("{}", "Failed to load detector");
            return;
        }

        MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(img, faceDetections);
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

    @Value
    static class FileInfo {
        //=> /home/hdm/project/RSP/images/test1.jpg
        private String sourcePath;

        public String getPrefixFilePath() {
            //=> /home/hdm/project/RSP/images
            return sourcePath.substring(0, sourcePath.lastIndexOf("/"));
        }

        public String getFileName() {
            String fileFullName = extractFileName();
            return fileFullName.substring(0, fileFullName.lastIndexOf("."));
        }

        public String getFileExtension() {
            String fileFullName = extractFileName();
            return fileFullName.substring(fileFullName.lastIndexOf("."));
        }

        public String getDestinationPath() {
            String destinationPath = getPrefixFilePath() + getFileName() + "_result" + getFileExtension();
            return destinationPath;
        }

        private String extractFileName() {
            return sourcePath.substring(sourcePath.lastIndexOf("/"));
        }
    }

}
