using System.CommandLine;
using SystemManager.Core;
using SystemManager.Model;
using SystemManager.Util;

namespace SystemManager;

// TODO: Review names and logs. Refactor namespaces.

public static class Program
{
    static async Task Main(string[] args)
    {
        CommandLine commandLine = [];
        SetupHdrActions(commandLine.HdrCommand);
        SetupLaunchAction(commandLine.LaunchCommand);
        SetupServiceAction(commandLine.ServiceCommand);

        ParseResult result = commandLine.Parse(args);

        try
        {
            VerbosityOption.Level level = result.GetRequiredValue(commandLine.VerbosityOption);
            Log.Level = level.Loglevel;
        }
        catch { }

        int resultCode = result.Invoke();
        Environment.Exit(resultCode);
    }

    private static void SetupHdrActions(HdrCommand command)
    {
        command.StatusCommand.SetAction(_ => Console.WriteLine($"{HdrManager.IsEnabled()}"));
        command.EnableCommand.SetAction(_ => HdrManager.SetHdrEnabled(true));
        command.DisableCommand.SetAction(_ => HdrManager.SetHdrEnabled(false));
    }

    private static void SetupLaunchAction(LaunchCommand command)
    {
        command.SetAction(parseResult =>
        {
            string path = parseResult.GetRequiredValue(command.PathArgument);
            var resolvedPath = Environment.ExpandEnvironmentVariables(path);
            Uri uri = new(resolvedPath);

            if (File.Exists(uri.LocalPath))
            {
                Application app = new(uri);
                Launcher.Launch(app);
                return 0;
            }
            else
            {
                return ErrorCode.ERROR_FILE_NOT_FOUND;
            }
        });
    }

    private static void SetupServiceAction(ServiceCommand command)
    {
        command.StartCommand.SetAction(parseResult =>
        {
            Log.D($"Start service");
        });
    }
}