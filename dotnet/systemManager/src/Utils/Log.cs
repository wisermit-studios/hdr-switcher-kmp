namespace SystemManager.Utils
{
    public static class Log
    {
        public const int LEVEL_NONE = -1;
        public const int LEVEL_ERROR = 3;
        public const int LEVEL_WARNING = 4;
        public const int LEVEL_INFO = 6;
        public const int LEVEL_DEBUG = 7;

        public static int Level { get; set; } = LEVEL_NONE;

        public static void E(string message)
        {
            WriteLog(LEVEL_ERROR, message);
        }

        public static void W(string message)
        {
            WriteLog(LEVEL_WARNING, message);
        }

        public static void I(string message)
        {
            WriteLog(LEVEL_INFO, message);
        }

        public static void D(string message)
        {
            WriteLog(LEVEL_DEBUG, message);
        }

        private static void WriteLog(int level, string message)
        {
            if (level <= Level)
            {
                string stringLevel = level switch
                {
                    LEVEL_ERROR => "E",
                    LEVEL_WARNING => "W",
                    LEVEL_INFO => "I",
                    LEVEL_DEBUG => "D",
                    _ => "_"
                };

                Console.WriteLine($"{stringLevel}/{message}");
            }
        }
    }
}