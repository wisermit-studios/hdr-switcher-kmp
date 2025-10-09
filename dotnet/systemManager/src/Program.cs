using System.Threading.Tasks;
using SystemManager.Core;
using SystemManager.Core.Models;
using SystemManager.Resources;
using SystemManager.Utils;

// TODO: Review names and logs.
namespace SystemManager
{
    public static class Program
    {
        private const int ERROR_FILE_NOT_FOUND = 0x2;
        private const int ERROR_INVALID_COMMAND_LINE = 0x667;
        private const int ERROR_BROKEN_PIPE = 0x6D;

        private static async Task Test()
        {
            using var reader = new StreamReader(Console.OpenStandardInput());

            string? line;
            while ((line = await reader.ReadLineAsync()) != null)
            {
                Log.D($"command: {line}");
            }
            reader.Close();
        }

        [STAThread]
        static async Task Main(string[] args)
        {
            var logLevel = LogLevel.Debug;
            Log.Level = logLevel;

            if (args.Length == 0)
            {
                Log.E("Invalid command line.");
                Environment.Exit(ERROR_INVALID_COMMAND_LINE);
            }

            Log.D($"Starting. Args({args.Length}): {string.Join(", ", args)}");

            if (args.Length == 1)
            {
                Log.D("Launching process.");
                LaunchExe(args[0]);
            }
            else
            {
                var task = Test();

                Log.D("Starting service.");
                StartService(args);

                await task.ContinueWith(t =>
                {
                    if (t.IsFaulted)
                    {
                        Console.Error.WriteLine($"Erro: {t.Exception}");
                    }
                    Environment.Exit(ERROR_BROKEN_PIPE);
                });
            }

            Application.Run();
        }

        private static void LaunchExe(string exePath)
        {
            if (File.Exists(exePath))
            {
                Task.Run(() =>
                    {
                        Launcher.Launch(exePath);
                    }
                );
                Environment.Exit(0);
            }
            else
            {
                MessageBox.Show(
                    Res.Strings.DialogErrorText + $"\"{exePath}\"",
                    Res.Strings.DialogErrorCaption,
                    MessageBoxButtons.OK
                );
                Environment.Exit(ERROR_FILE_NOT_FOUND);
            }
        }

        private static void StartService(string[] args)
        {
            var exeList = Exe.ListFromArgs(args);
            var manager = new Service();
            manager.Watch(exeList);
        }
    }
}
