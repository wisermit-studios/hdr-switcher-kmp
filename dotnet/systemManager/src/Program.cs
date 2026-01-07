using System.CommandLine;
using SystemManager.Console;
using SystemManager.Core;
using SystemManager.Model;
using SystemManager.Util;

namespace SystemManager;

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
        command.StatusCommand.SetAction(_ => System.Console.WriteLine($"{HdrManager.IsEnabled()}"));
        command.EnableCommand.SetAction(_ => HdrManager.SetHdrEnabled(true));
        command.DisableCommand.SetAction(_ => HdrManager.SetHdrEnabled(false));
    }

    private static void SetupLaunchAction(LaunchCommand command)
    {
        command.SetAction(parseResult =>
        {
            string path = parseResult.GetRequiredValue(command.PathArgument);
            Application app = new(path.ResolvedPath());

            if (app.File.Exists)
            {
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
        command.StartCommand.SetAction(async (parseResult, cancellationToken) =>
        {
            string dataPath = parseResult.GetRequiredValue(command.StartCommand.DataOption);
            FileInfo dataFile = new(dataPath.ResolvedPath());
            
            Service service = new(
                dataFile,
                onError: code => Environment.Exit(code)
            );

            cancellationToken.Register(service.Stop);

            await InputReader.Listen(command =>
            {
                // FIXME: Remove test.
                Log.D($"command: {command}");
            });
            return ErrorCode.ERROR_BROKEN_PIPE;
        });
    }
}