namespace SystemManager.Util;

public static class Extensions
{
    public static string ResolvedPath(this string path)
    {
        string resolvedPath = Environment.ExpandEnvironmentVariables(path);
        // TODO: Resolve redirections.
        return new Uri(resolvedPath).LocalPath;
    }
}