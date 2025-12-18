namespace SystemManager.Model;

public enum HdrMode
{
    Default,
    On,
    Off
}

public record Application(
    string Path,
    HdrMode Hdr = HdrMode.Default
)
{
    public FileInfo File = new(Path);
}