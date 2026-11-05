package com.demo.controller;


import com.demo.rag.DocumentIngestor;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.allminilml6v2.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: Linda
 * @date: 2026/10/28 16:47
 * @description:
 */
@RestController
@RequestMapping("/api/rag")
public class RagDebugController {
    private final EmbeddingStore<TextSegment> embeddingStore;
    private final EmbeddingModel embeddingModel = new AllMiniLmL6V2EmbeddingModel();

    public RagDebugController(DocumentIngestor documentIngestor){
        this.embeddingStore = documentIngestor.getEmbeddingStore();
    }

    @GetMapping("/search")
    public List<String> search(@RequestParam String query){
        Embedding queryEmbedding = embeddingModel.embed(query).content();
        EmbeddingSearchRequest request = EmbeddingSearchRequest.builder()
                .queryEmbedding(queryEmbedding)
                .maxResults(3)
                .build();

        return embeddingStore.search(request).matches().stream()
                .filter(match -> match.score() >= 0.6)// 低于阈值的丢掉
                .map(match -> {
                    double score = match.score();
                    String text = match.embedded().text();
                    return String.format("[score=%.4f] %s", score,
                            text.length() > 200 ? text.substring(0, 200) + "..." : text);
                })
                .toList();

    }


}
