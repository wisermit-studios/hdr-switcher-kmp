using SystemManager.Util;
using SystemManager.Model;
using System.Text.Json;
using System.Diagnostics;
using System.Text.Json.Serialization;

namespace SystemManager.Core;

public class Service
{
    private static readonly JsonSerializerOptions s_jsonOptions = new()
    {
        PropertyNameCaseInsensitive = true,
        Converters = { new JsonStringEnumConverter(JsonNamingPolicy.CamelCase) }
    };

    private readonly ProcessWatcher _processWatcher = new();

    public Service(FileInfo dataFile, Action<int> onError)
    {
        Log.I($"Starting service. Data={dataFile}.");

        Result result = LoadData(dataFile);
        
        switch (result)
        {
            case Result.Success data:
                List<Application> applications = data.Applications;
                if (applications.Count == 0)
                {
                    Log.I($"No applications to watch.");
                    onError(ErrorCode.ERROR_NO_DATA);
                }
                else
                {
                    Log.I($"Watching ({applications.Count}) applications.");
                    WatchApplications(applications);
                }
                break;
            case Result.Error error:
                onError(error.Code);
                break;
            default:
                // Never happens.
                throw new UnreachableException();
        }
    }

    public void WatchApplications(List<Application> applications)
    {
        _processWatcher?.Watch(
            applications,
            onStart: () =>
            {
                Log.I($"Process started.");
                HdrManager.SetHdrEnabled(true);
            },
            onFinish: () =>
            {
                Log.I($"Process finished.");
                HdrManager.SetHdrEnabled(false);
            }
        );
    }

    public void Stop()
    {
        Log.I($"Service stopped.");
        _processWatcher.Stop();
    }

    private static Result LoadData(FileInfo dataFile)
    {
        if (!dataFile.Exists)
        {
            return new Result.Error(ErrorCode.ERROR_FILE_NOT_FOUND);
        }

        try
        {
            string json = File.ReadAllText(dataFile.FullName);
            ApplicationsData data = JsonSerializer.Deserialize<ApplicationsData>(json, s_jsonOptions)!;
            return new Result.Success(data.Applications);
        }
        catch (Exception e)
        {
            Log.E(e.Message);
            return new Result.Error(ErrorCode.ERROR_INVALID_DATA);
        }
    }

    private abstract record Result
    {
        public record Success(List<Application> Applications) : Result;
        public record Error(int Code) : Result;
    }
}