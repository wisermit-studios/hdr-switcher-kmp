namespace SystemManager.Model;

public class Application(Uri uri, bool? hdrStatus = null)
{
    public Uri Uri { get; } = uri;
    public string Name { get; } = Path.GetFileName(uri.LocalPath);
    public bool? HdrStatus { get; } = hdrStatus;
}