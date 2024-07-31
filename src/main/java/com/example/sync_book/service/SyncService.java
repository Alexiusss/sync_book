package com.example.sync_book.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SyncService {
    @Value("${default.audio.chunk.size}")
    private Long defaultAudioChunkSize;

    public long calculateStartRange(long fileSize, float lastPosition) {
        return (int) (fileSize * lastPosition / 100);
    }

    public long calculateEndRange(long fileSize, long startRange) {
        long endRange = startRange + defaultAudioChunkSize;
        return Math.min(endRange, fileSize);
    }
}