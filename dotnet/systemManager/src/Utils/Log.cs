namespace SystemManager.Utils
{
    public enum LogLevel
    {
        None = -1,
        Error = 3,
        Warning = 4,
        Info = 6,
        Debug = 7
    }

    public static class Log
    {
        public static LogLevel Level { get; set; } = LogLevel.None;

        public static void E(string message)
        {
            WriteLog(LogLevel.Error, message);
        }

        public static void W(string message)
        {
            WriteLog(LogLevel.Warning, message);
        }

        public static void I(string message)
        {
            WriteLog(LogLevel.Info, message);
        }

        public static void D(string message)
        {
            WriteLog(LogLevel.Debug, message);
        }

        private static void WriteLog(LogLevel level, string message)
        {
            if (level <= Level)
            {
                char logPrefix = level.ToString()[0];

                Console.WriteLine($"{logPrefix}/{message}");
            }
        }
    }
}