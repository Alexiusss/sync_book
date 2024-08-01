package com.example.sync_book.util;

import com.example.sync_book.error.IllegalRequestDataException;
import lombok.experimental.UtilityClass;

/**
 * Utility class for handling audio-related operations.
 */
@UtilityClass
public class AudioUtil {
    public static final String AUDIO_MPEG = "audio/mpeg";
    public static final String ACCEPTS_RANGES_VALUE = "bytes";

    public static final int DEFAULT_CHUNK_SIZE = 4194304;

    /**
     * Parses the start range from a range header.
     *
     * @param range the range header value
     * @return the start range
     * @throws IllegalRequestDataException if the range cannot be parsed
     */
    public static long parseStartRange(String range) {
        try {
            return Long.parseLong(getRange(range)[0]);
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            throw new IllegalRequestDataException("Invalid Range header format: '" + range + "'. Expected format: 'bytes=<start>-<end>'. Example: 'bytes=0-1000'.");
        }
    }

    /**
     * Parses the end range from a range header or calculates it if not provided.
     *
     * @param range    the range header value
     * @param fileSize the total size of the file
     * @return the end range or DEFAULT_CHUNK_SIZE if invalid
     * @throws IllegalRequestDataException if the range cannot be parsed
     */
    public static long parseEndRange(String range, long fileSize) {
        long endRange;
        String[] arrRange = getRange(range);
        try {
            if (arrRange.length < 2 || arrRange[1].isEmpty()) {
                endRange = parseStartRange(range) + DEFAULT_CHUNK_SIZE;
            } else {
                endRange = Long.parseLong(arrRange[1]);
            }
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            throw new IllegalRequestDataException("Invalid Range header format: '" + range + "'. Expected format: 'bytes=<start>-<end>'. Example: 'bytes=0-1000'.");
        }
        return Math.min(endRange, fileSize);
    }

    /**
     * Extracts the range values from a range header.
     *
     * @param range the range header value
     * @return an array of range values as strings
     */
    private static String[] getRange(String range) {
        return range.replace("bytes=", "").split("-");
    }

    /**
     * Calculates the content length for the specified range.
     *
     * @param startRange the start of the range
     * @param endRange   the end of the range
     * @return the content length as a string
     */
    public static String calculateContentLength(long startRange, long endRange) {
        return String.valueOf(endRange - startRange);
    }

    /**
     * Constructs the content range value for a response header.
     *
     * @param startRange the start of the range
     * @param endRange   the end of the range
     * @param fileSize   the total size of the file
     * @return the content range value
     */
    public static String getContentRangeValue(long startRange, long endRange, long fileSize) {
        return "bytes " + startRange + "-" + endRange + "/" + fileSize;
    }
}