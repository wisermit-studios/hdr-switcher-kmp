using System.Text.Json.Serialization;

namespace SystemManager.Model;

public record ApplicationsData(
    int Version,
    [property: JsonPropertyName("data")] List<Application> Applications
);