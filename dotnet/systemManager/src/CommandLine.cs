using System.CommandLine;
using SystemManager.Util;

namespace SystemManager;

class CommandLine : RootCommand
{

    public readonly HdrCommand HdrCommand = [];
    public readonly LaunchCommand LaunchCommand = [];
    public readonly ServiceCommand ServiceCommand = [];

    public readonly VerbosityOption VerbosityOption = new();

    public CommandLine() : base("HDR Switcher")
    {
        Add(HdrCommand);
        Add(LaunchCommand);
        Add(ServiceCommand);

        Add(VerbosityOption);
    }
}

class HdrCommand : Command
{
    public readonly Command StatusCommand = new("status", "Display the HDR status.");
    public readonly Command EnableCommand = new("enable", "Enabled HDR.");
    public readonly Command DisableCommand = new("disable", "Disable HDR.");

    public HdrCommand() : base("hdr", "Manage HDR status.")
    {
        Add(StatusCommand);
        Add(EnableCommand);
        Add(DisableCommand);
    }
}

class LaunchCommand : Command
{
    public readonly Argument<string> PathArgument = new("path");

    public LaunchCommand() : base("launch", "Launch an application with custom settings.")
    {
        Add(PathArgument);
    }
}

class ServiceCommand : Command
{
    public readonly Command StartCommand = new("start", "Start the service.");
    public readonly Command StopCommand = new("stop", "Stop the service.");

    public ServiceCommand() : base("service", "Manage the custom applications settings service.")
    {
        Add(StartCommand);
        Add(StopCommand);
    }
}

class VerbosityOption : Option<VerbosityOption.Level>
{
    public record Level(string Name, string Alias, LogLevel Loglevel)
    {
        public readonly static Level Quiet = new("quiet", "q", LogLevel.Error);
        public readonly static Level Minimal = new("minimal", "m", LogLevel.Warning);
        public readonly static Level Normal = new("normal", "n", LogLevel.Warning);
        public readonly static Level Detailed = new("detailed", "d", LogLevel.Info);
        public readonly static Level Diagnostic = new("diagnostic", "diag", LogLevel.Debug);

        public readonly static Level[] Entries = [Quiet, Minimal, Normal, Detailed, Diagnostic];

        public static Level Resolve(string value) => Entries.First(it => it.Values.Contains(value));

        public readonly IReadOnlyList<string> Values = [Alias, Name];

        public override string ToString() => Name;
    };

    public static Level Default = Level.Normal;

    public VerbosityOption() : base("verbosity", "-v")
    {
        Description = $"Verbosity level. Allowed values: " +
            $"{string.Join(", ", Level.Entries.Select(x => $"{x.Alias}({x.Name})"))}.";
        HelpName = "LEVEL";
        Recursive = true;
        DefaultValueFactory = result => Default;
        CustomParser = result => Level.Resolve(result.Tokens[0].Value);

        string[] values = [.. Level.Entries.SelectMany(it => it.Values)];
        AcceptOnlyFromAmong(values);
    }
}