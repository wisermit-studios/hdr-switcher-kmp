namespace SystemManager.Util;

public enum LogLevel
{
    Error = 3,
    Warning = 4,
    Info = 6,
    Debug = 7
}

public static class Log
{
    public static LogLevel Level { get; set; } = LogLevel.Error;

    private static readonly string END_MESSAGE_DELIMITER = "\u00A0";

    static Log()
    {
        System.Console.OutputEncoding = System.Text.Encoding.UTF8;
    }

    public static void E(string message)
    {
        WriteLine(LogLevel.Error, message);
    }

    public static void W(string message)
    {
        WriteLine(LogLevel.Warning, message);
    }

    public static void I(string message)
    {
        WriteLine(LogLevel.Info, message);
    }

    public static void D(string message)
    {
        WriteLine(LogLevel.Debug, message);
    }

    private static void WriteLine(LogLevel level, string message)
    {
        if (level <= Level)
        {
            char logPrefix = level.ToString()[0];

            System.Console.WriteLine($"{logPrefix}/{message}{END_MESSAGE_DELIMITER}");
        }
    }
}