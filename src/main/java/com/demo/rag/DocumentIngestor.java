package com.demo.rag;


import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.allminilml6v2.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;

/**
 * @author: Linda
 * @date: 2026/10/28 16:43
 * @description:
 */
@Service
public class DocumentIngestor {
    private final EmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
    private EmbeddingModel embeddingModel;

    @PostConstruct
    public void ingest() {
        // 1. 加载文档
       /*

          Document document = FileSystemDocumentLoader.loadDocument(
                "src/main/resources/docs/knowledge.txt",
                new TextDocumentParser()
        );
        System.out.println("文档加载完成，字符数: " + document.text().length());

        */

        try {
            ClassPathResource resource = new ClassPathResource("docs/sql.txt");
            try (InputStream is = resource.getInputStream()) {
                Document document = new TextDocumentParser().parse(is);
                // ...后面不变
                System.out.println("文档加载完成，字符数: " + document.text().length());

                // 2. 创建 Embedding 模型（本地运行，首次会下载模型文件）
                embeddingModel = new AllMiniLmL6V2EmbeddingModel();

                // 3. 构建 Ingestor：切分 + 向量化 + 存储
                EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
//                .documentSplitter(DocumentSplitters.recursive(200, 20))
//                .documentSplitter(DocumentSplitters.recursive(100, 10))
                        .documentSplitter(DocumentSplitters.recursive(1000, 100))
                        .embeddingModel(embeddingModel)
                        .embeddingStore(embeddingStore)
                        .build();

                // 4. 执行
                ingestor.ingest(document);
                System.out.println("向量化完成，已存入内存");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public EmbeddingStore<TextSegment> getEmbeddingStore() {
        return embeddingStore;
    }

    public EmbeddingModel getEmbeddingModel(){
        return embeddingModel;
    }
}
