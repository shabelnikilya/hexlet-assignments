package exercise;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

class App {

    // BEGIN
    public static CompletableFuture<String> unionFiles(
            String firstFilePath, String secondFilePath, String resultFilePath) {
        CompletableFuture<StringBuffer> firstRead = CompletableFuture.supplyAsync(() -> {
            StringBuffer rsl = new StringBuffer();
            readFile(firstFilePath, rsl);
            return rsl;
        });

        CompletableFuture<StringBuffer> secondRead = CompletableFuture.supplyAsync(() -> {
            StringBuffer rsl = new StringBuffer();
            readFile(secondFilePath, rsl);
            return rsl;
        });

        return firstRead.thenCombine(secondRead, (firstFile, secondFile) -> {
            String text = firstFile.append(secondFile.toString()).toString();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(resultFilePath))) {
                writer.write(text);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return text;
        });
    }

    private static void readFile(String firstFilePath, StringBuffer rsl) {
        try {
            Path path = Paths.get(firstFilePath);
            rsl.append(Files.readString(path));
        } catch (IOException e) {
            System.out.println(e);
        }

    }
    // END

    public static CompletableFuture<Long> getDirectorySize(String directory) {
        Path pathFile = Paths.get(directory);
        if (!Files.isDirectory(pathFile)) {
            throw new RuntimeException();
        }

        try {
            CompletableFuture<Long> resultFuture = Files.list(pathFile)
                    .filter(p -> !Files.isDirectory(p))
                    .map(file -> CompletableFuture.supplyAsync(() -> {
                        try {
                            return Files.size(file);
                        } catch (IOException e) {
                            e.printStackTrace();
                            return 0L;
                        }
                    }))
                    .collect(Collectors.collectingAndThen(
                            Collectors.toList(),
                            list -> CompletableFuture.allOf(list.toArray(new CompletableFuture[0]))
                                    .thenApply(v -> list.stream()
                                            .mapToLong(CompletableFuture::join)
                                            .sum())
                    ));

            return resultFuture;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws Exception {
        // BEGIN
        String directory = "src/main/resources";
        System.out.println(getDirectorySize(directory).get());
        // END
    }
}

