using System.Text.Json.Serialization;

namespace SystemManager.Model;

public enum HdrMode
{
    Default,
    On,
    Off
}

public record Application(
    [property: JsonPropertyName("path")] FileInfo File,
    [property: JsonPropertyName("hdr")] HdrMode Hdr = HdrMode.Default
);