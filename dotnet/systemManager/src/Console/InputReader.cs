using SystemManager.Util;

namespace SystemManager.Console;

public static class InputReader
{
    public static async Task Listen(Action<string> action)
    {
        try
        {
            using Stream stream = System.Console.OpenStandardInput();
            using StreamReader reader = new(stream);

            while (await reader.ReadLineAsync() is { } line)
            {
                action(line);
            }
        }
        catch (Exception e)
        {
            Log.E($"Error {e}");
        }
    }
}