package latmod.lib.github;

import com.google.gson.*;
import latmod.lib.util.FinalIDObject;

import java.util.*;

/**
 * Created by LatvianModder on 16.03.2016.
 */
public class GitHubBranch extends FinalIDObject
{
	public final GitHubRepo repo;
	
	//public final GitHubCommit lastCommit;
	private Map<String, GitHubCommit> commits;
	
	public GitHubBranch(GitHubRepo r, JsonObject o)
	{
		super(o.get("name").getAsString());
		//lastCommit = new GitHubCommit(this, o.get("commit").getAsJsonObject());
		repo = r;
	}
	
	public Map<String, GitHubCommit> getCommits()
	{
		if(commits == null)
		{
			Map<String, GitHubCommit> map = new LinkedHashMap<>();
			
			try
			{
				JsonArray a = GitHubAPI.getAPI("repos/" + repo.getID() + "/commits?sha=" + getID()).getAsJsonArray();
				
				for(JsonElement e : a)
				{
					GitHubCommit b = new GitHubCommit(this, e.getAsJsonObject());
					map.put(b.getID(), b);
				}
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
			
			commits = Collections.unmodifiableMap(map);
		}
		
		return commits;
	}
	
	public String getFilePath(String s)
	{ return GitHubAPI.RAW_CONTENT + repo.getID() + "/" + getID() + "/" + s; }
	
	public String getZipDownloadURL()
	{ return "https://github.com/" + repo.getID() + "/archive/" + getID() + ".zip"; }
}