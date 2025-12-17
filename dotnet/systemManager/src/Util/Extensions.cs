namespace SystemManager.Util;

public static class Extensions
{
    public static string ResolvedPath(this string path)
    {
        var resolvedPath = Environment.ExpandEnvironmentVariables(path);
        return new Uri(resolvedPath).LocalPath;
    }
}