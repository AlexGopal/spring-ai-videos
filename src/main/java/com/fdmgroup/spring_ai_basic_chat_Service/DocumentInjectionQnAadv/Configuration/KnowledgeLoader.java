package com.fdmgroup.spring_ai_basic_chat_service.DocumentInjectionQnAadv.Configuration;

import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

// Register this class as a source of Spring bean definitions.
@Configuration
public class KnowledgeLoader {

    // Create a runner that loads knowledge when the application starts.
    @Bean
    CommandLineRunner loadKnowledge(

            // Qualifiers select which VectorStore bean Spring injects.
            @Qualifier("vectorStore") VectorStore vectorStore,
            @Qualifier("hrVectorStore") VectorStore hrVectorStore,
            @Qualifier("itVectorStore") VectorStore itVectorStore,
            @Qualifier("policyVectorStore") VectorStore policyVectorStore,

            // Currently unused; the resolver below finds our resources instead.
            ResourceLoader resourceLoader) {

        return args -> {

            /*
             * KnowledgeLoader handles the INGESTION side of RAG:
             *
             * Company.txt -> Reader -> Documents -> Chunks -> VectorStore
             *
             * PathMatchingResourcePatternResolver:
             * - Finds the original source resource using a path.
             * - Can also resolve patterns matching multiple resources.
             *
             * VectorStore:
             * - We ADD the processed knowledge here.
             * - ChatConfig later gives this VectorStore to QuestionAnswerAdvisor
             *   so relevant knowledge can be retrieved during LLM requests.
             */

            // Find resources using specific paths or matching file patterns.
            PathMatchingResourcePatternResolver resolver =
                    new PathMatchingResourcePatternResolver();

            // Configure how the text will be split into smaller chunks.
            TokenTextSplitter splitter = TokenTextSplitter.builder()
                    .withChunkSize(500)             // Target roughly 500 tokens per chunk.
                    .withMinChunkSizeChars(100)     // Threshold for choosing a sentence boundary.
                    .withMinChunkLengthToEmbed(5)   // Character threshold to filter tiny chunks.
                    .withMaxNumChunks(1000)         // Limit splitting iterations per document.
                    .withKeepSeparator(true)        // Preserve separators such as line breaks.
                    .build();

            // Track the combined chunk count from our loading calls.
            int totalChunks = 0;

            // Load ONLY Company.txt into the main vector store.
            // HR, IT, and policy stores need their own loadIntoStore(...) calls.
            totalChunks += loadIntoStore(resolver, "classpath:Doc/Company.txt", vectorStore, splitter);

            totalChunks += loadIntoStore(resolver, "classpath:Doc/hr.txt", hrVectorStore, splitter);

            totalChunks += loadIntoStore(resolver, "classpath:Doc/it.txt", itVectorStore, splitter);

            totalChunks += loadIntoStore(resolver, "classpath:Doc/policy.txt", policyVectorStore, splitter);
            // totalChunks is currently collected but not printed or used further.

            System.out.println("total chunks loaded" + totalChunks);
        };
    }

    // Reuse the same reading/splitting logic for a supplied path and target store.
    private int loadIntoStore(
            PathMatchingResourcePatternResolver resolver,
            String path,
            VectorStore targetStore,
            TokenTextSplitter splitter) throws Exception {

        // Resolve all resources matching this path (currently one specific file).
        Resource[] resources = resolver.getResources(path);

        // Count chunks added during this helper call.
        int chunksLoaded = 0;

        // Process each matching file.
        for (Resource resource : resources) {

            // Read the file into Documents containing text and metadata.
            // Reading does not perform our TokenTextSplitter chunking yet.
            TextReader reader = new TextReader(resource);
            List<Document> documents = reader.get();

            // Split all input Documents into smaller Documents (chunks).
            // The source metadata is carried into the resulting chunks.
            List<Document> chunks = splitter.apply(documents);

            // Add chunks to the chosen store; an embedding-based store generates
            // embeddings using its configured model and stores them for retrieval.
            targetStore.add(chunks);

            // Update the count and report how many chunks this file produced.
            chunksLoaded += chunks.size();

            System.out.println(
                    "loaded" + resource.getFilename()
                            + " => " + chunks.size() + "chunks"
            );
        }

        // Return this call's chunk count to the startup runner.
        return chunksLoaded;
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