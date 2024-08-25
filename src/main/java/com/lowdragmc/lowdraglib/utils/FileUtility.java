package com.lowdragmc.lowdraglib.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileAttribute;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.io.IOUtils;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/utils/FileUtility.class */
public class FileUtility {
    public static final JsonParser jsonParser = new JsonParser();
    public static final Gson GSON_PRETTY = new GsonBuilder().setPrettyPrinting().create();

    private FileUtility() {
    }

    public static String readInputStream(InputStream inputStream) throws IOException {
        byte[] streamData = IOUtils.toByteArray(inputStream);
        return new String(streamData, StandardCharsets.UTF_8);
    }

    public static InputStream writeInputStream(String contents) {
        return new ByteArrayInputStream(contents.getBytes(StandardCharsets.UTF_8));
    }

    public static JsonObject tryExtractFromFile(Path filePath) {
        try {
            InputStream fileStream = Files.newInputStream(filePath, new OpenOption[0]);
            InputStreamReader streamReader = new InputStreamReader(fileStream);
            JsonObject asJsonObject = jsonParser.parse(streamReader).getAsJsonObject();
            if (fileStream != null) {
                fileStream.close();
            }
            return asJsonObject;
        } catch (Exception e) {
            return null;
        }
    }

    public static JsonElement loadJson(File file) {
        try {
            if (file.isFile()) {
                Reader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
                JsonElement json = jsonParser.parse(new JsonReader(reader));
                reader.close();
                return json;
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean saveJson(File file, JsonElement element) {
        try {
            if (!file.getParentFile().isDirectory()) {
                file.getParentFile().mkdirs();
            }
            Writer writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
            writer.write(GSON_PRETTY.toJson(element));
            writer.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void extractJarFiles(String resource, File targetPath, boolean replace) {
        Path resourcePath;
        FileSystem zipFileSystem = null;
        try {
            URI sampleUri = FileUtility.class.getResource("/assets/gregtech/.gtassetsroot").toURI();
            if (sampleUri.getScheme().equals("jar") || sampleUri.getScheme().equals("zip")) {
                zipFileSystem = FileSystems.newFileSystem(sampleUri, Collections.emptyMap());
                resourcePath = zipFileSystem.getPath(resource, new String[0]);
            } else if (sampleUri.getScheme().equals("file")) {
                resourcePath = Paths.get(FileUtility.class.getResource(resource).toURI());
            } else {
                throw new IllegalStateException("Unable to locate absolute path to directory: " + sampleUri);
            }
            List<Path> jarFiles = (List) Files.walk(resourcePath, new FileVisitOption[0]).filter(x$0 -> {
                return Files.isRegularFile(x$0, new LinkOption[0]);
            }).collect(Collectors.toList());
            for (Path jarFile : jarFiles) {
                Path genPath = targetPath.toPath().resolve(resourcePath.relativize(jarFile).toString());
                Files.createDirectories(genPath.getParent(), new FileAttribute[0]);
                if (replace || !genPath.toFile().isFile()) {
                    Files.copy(jarFile, genPath, StandardCopyOption.REPLACE_EXISTING);
                }
            }
            if (zipFileSystem != null) {
                IOUtils.closeQuietly(zipFileSystem);
            }
        } catch (IOException | URISyntaxException e) {
            if (0 != 0) {
                IOUtils.closeQuietly((Closeable) null);
            }
        } catch (Throwable th) {
            if (0 != 0) {
                IOUtils.closeQuietly((Closeable) null);
            }
            throw th;
        }
    }
}
