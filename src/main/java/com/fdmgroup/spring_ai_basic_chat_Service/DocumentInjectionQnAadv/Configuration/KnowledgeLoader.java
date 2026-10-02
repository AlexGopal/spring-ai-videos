package com.fdmgroup.spring_ai_basic_chat_Service.DocumentInjectionQnAadv.Configuration;

import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

@Configuration
public class KnowledgeLoader {

    @Bean
    CommandLineRunner loadKnowledge(
            VectorStore vectorStore,
            ResourceLoader resourceLoader) {

        return args -> {

            /*
             * KnowledgeLoader handles the INGESTION side of RAG:
             *
             * Company.txt -> Reader -> Documents -> Chunks -> VectorStore
             *
             * ResourceLoader:
             * - Finds the original source resource (Company.txt).
             *
             * VectorStore:
             * - We ADD the processed knowledge here.
             * - ChatConfig later gives this VectorStore to QuestionAnswerAdvisor
             *   so relevant knowledge can be retrieved during LLM requests.
             */

            var resource =
                resourceLoader.getResource("classpath:doc/Company.txt");

            var reader = new TextReader(resource);

            /*
             * reader.get():
             * - Reads the source resource.
             * - Extracts its CONTENT into Spring AI Document object(s).
             * - Documents can also contain metadata about the content/resource.
             * - This does NOT perform our chunking yet.
             *
             * TextReader will normally give us one Document for this text file,
             * but the API returns List<Document> because other readers can
             * produce multiple Documents. such as PagePdfDocumentReader
             
                Resource pdfResource =
                resourceLoader.getResource(
                    "classpath:doc/CompanyHandbook.pdf"
                );

            PagePdfDocumentReader reader =
                new PagePdfDocumentReader(pdfResource);

            List<Document> docs = reader.get();
             */

            // this will automatically go through each source file we dont need a forloop
            // or stream to do it and produce the different documents
            List<Document> docs = reader.get();

            /*
             * Configure how the Documents should be split into smaller chunks.
             * The chunks are also Spring AI Document objects.
             */
            TokenTextSplitter splitter = TokenTextSplitter.builder()
                .withChunkSize(500)
                .withMinChunkSizeChars(100)
                .withMinChunkLengthToEmbed(5)
                .withMaxNumChunks(1000)
                .withKeepSeparator(true)
                .build();

            /*
             * Chunking happens HERE.
             *
             * splitter.apply(docs) processes the entire List<Document>.
             * We do not need to write our own for-loop or stream.
             *
             * List<Document> docs
             *          ↓
             * TokenTextSplitter
             *          ↓
             * List<Document> chunks
             */
            List<Document> chunks = splitter.apply(docs);

            /*
             * Add the smaller chunk Documents to the VectorStore.
             * They can later be retrieved by QuestionAnswerAdvisor for RAG.
             *
             * For the full explanation, see:
             * spring-ai-knowledge-loader-documents-chunking-lesson.md
             */
            vectorStore.add(chunks);

            System.out.println("Loaded " + chunks.size()+ " documents into the vector store");
        };
    }
}


// Example of how this looks

/*
 * reader.get():
 * - Reads the source resource and creates Spring AI Document object(s).
 * - Each Document contains CONTENT + METADATA.
 * - We do NOT need to manually loop through the resource's contents. for reader.get();
 * - This does NOT perform our TokenTextSplitter chunking yet.
 *
 * Example with a PDF:
 *
 * Resource pdfResource =
 *     resourceLoader.getResource("classpath:doc/CompanyHandbook.pdf");
 *
 * PagePdfDocumentReader pdfReader =
 *     new PagePdfDocumentReader(pdfResource);
 *
 * List<Document> pdfDocs = pdfReader.get();
 *
 * Conceptually, a page-based PDF reader could give us:
 *
 * pdfDocs = [
 *     Document A {
 *         CONTENT: page 1 text,
 *         METADATA: page/source information
 *     },
 *
 *     Document B {
 *         CONTENT: page 2 text,
 *         METADATA: page/source information
 *     },
 *
 *     Document C {
 *         CONTENT: page 3 text,
 *         METADATA: page/source information
 *     }
 * ];
 *
 * Then chunk ALL of those Documents:
 *
 * List<Document> pdfChunks = splitter.apply(pdfDocs);
 *
 * Conceptually:
 *
 * Document A -> Chunk A1, A2, A3
 * Document B -> Chunk B1, B2
 * Document C -> Chunk C1, C2, C3
 *
 * The chunks are ALSO Document objects, so they still have
 * CONTENT + METADATA. The content is now a smaller piece, while
 * the original Document's metadata is carried into its chunks.
 *
 * For example:
 *
 * Chunk A1 {
 *     CONTENT: first chunk of page 1 text,
 *     METADATA: page/source information from Document A
 * }
 *
 * Chunk A2 {
 *     CONTENT: second chunk of page 1 text,
 *     METADATA: page/source information from Document A
 * }
 *
 * pdfChunks = [A1, A2, A3, B1, B2, C1, C2, C3];
 *
 * So:
 *
 * pdfReader.get()
 *     -> reads the source
 *     -> creates Document(s) with CONTENT + METADATA
 *
 * splitter.apply(pdfDocs)
 *     -> processes ALL Documents
 *     -> splits their content into smaller chunks
 *     -> resulting chunks are also Documents with CONTENT + METADATA
 *
 * No manual for-loop or stream is needed. splitter.apply(pdfDocs)
 */