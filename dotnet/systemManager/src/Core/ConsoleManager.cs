using SystemManager.Util;

namespace SystemManager.Core;

public static class ConsoleManager
{
    public static async void ReadArgs(string[] args)
    {
        if (args.Length == 0)
        {
            Log.E("Invalid command line.");
            Environment.Exit(ErrorCode.ERROR_INVALID_COMMAND_LINE);
        }

        Log.D($"Starting. Args({args.Length}): {string.Join(", ", args)}");
    }

    public static async Task ListenInput()
    {
        var task = ReadInput();

        await task.ContinueWith(t =>
        {
            if (t.IsFaulted)
            {
                Log.E($"Error {t.Exception}");
            }
            Environment.Exit(ErrorCode.ERROR_BROKEN_PIPE);
        });
    }

    private static async Task ReadInput()
    {
        using var reader = new StreamReader(Console.OpenStandardInput());

        string? line;
        while ((line = await reader.ReadLineAsync()) != null)
        {
            Log.D($"command: {line}");
        }
        reader.Close();
    }
}