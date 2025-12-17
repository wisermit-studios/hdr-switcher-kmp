using SystemManager.Util;

namespace SystemManager.Console;

public static class InputReader
{
    public static async Task ListenInput()
    {
        await ReadInput().ContinueWith(task =>
        {
            if (task.IsFaulted)
            {
                Log.E($"Error {task.Exception}");
            }
            Environment.Exit(ErrorCode.ERROR_BROKEN_PIPE);
        });
    }

    private static async Task ReadInput()
    {
        using var reader = new StreamReader(System.Console.OpenStandardInput());
        string? line;
        while ((line = await reader.ReadLineAsync()) != null)
        {
            // FIXME: Remove test.
            Log.D($"command: {line}");
        }
        reader.Close();
    }
}