import lombok.SneakyThrows;

public final class ResourcePathFinder {

    @SneakyThrows
    public String getClassPathResource(String path) {
        String classPath = this.getClass().getResource(path).toURI().getPath();
        return classPath;
    }
}
