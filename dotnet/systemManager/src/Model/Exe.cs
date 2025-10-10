namespace SystemManager.Model
{
    public class Exe(string name, Uri uri)
    {
        public string Name { get; } = name;
        public Uri Uri { get; } = uri;

        public static List<Exe> ListFromArgs(string[] args)
        {
            return [.. args
                .Select(arg => arg.Split('|', 2))
                .Select(parts =>
                    new Exe(
                        name: parts[0],
                        uri: new Uri(parts[1])
                    )
                )
            ];
        }
    }
}