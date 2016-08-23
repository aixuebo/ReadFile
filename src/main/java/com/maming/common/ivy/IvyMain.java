package com.maming.common.ivy;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.apache.ivy.Ivy;
import org.apache.ivy.core.module.descriptor.DefaultDependencyDescriptor;
import org.apache.ivy.core.module.descriptor.DefaultModuleDescriptor;
import org.apache.ivy.core.module.id.ModuleRevisionId;
import org.apache.ivy.core.report.ResolveReport;
import org.apache.ivy.core.resolve.ResolveOptions;
import org.apache.ivy.core.retrieve.RetrieveOptions;
import org.apache.ivy.core.settings.IvySettings;
import org.apache.ivy.plugins.matcher.GlobPatternMatcher;
import org.apache.ivy.plugins.repository.file.FileRepository;
import org.apache.ivy.plugins.resolver.ChainResolver;
import org.apache.ivy.plugins.resolver.FileSystemResolver;
import org.apache.ivy.plugins.resolver.IBiblioResolver;

public class IvyMain {

	private String packages = "redis.clients:jedis:2.5.1,mysql/mysql-connector-java/5.1.29";//按照,拆分,每一组是一个maven坐标,分别用:或者/进行拆分,例如redis.clients:jedis:2.5.1,mysql/mysql-connector-java/5.1.29,表示需要的maven上的jar包
	private String packagesExclusions = "";//存储不需要的包,多个包用逗号拆分
	private String repositories = "http://10.1.5.102:8081/nexus/content/groups/public/,http://repo2.maven.org/maven2/";//maven资源集合
	private String ivyRepoPath = "E:\\upload\\ivy";//本地的maven仓库位置
	String ivyConfName = "default";
	
	public void test1(){
		
		IvySettings ivySettings = new IvySettings();
		System.out.println(ivySettings.getDefaultIvyUserDir());// C:\Users\Lenovo\.ivy2\jar 默认路径
		
        ivySettings.setDefaultIvyUserDir(new File(ivyRepoPath));//设置资源路径
        ivySettings.setDefaultCache(new File(ivyRepoPath, "cache"));
        File packagesDirectory = new File(ivyRepoPath, "jars");//设置maven的资源路径/jar
        System.out.println(ivySettings.getDefaultIvyUserDir());// C:\Users\Lenovo\.m2\repository
        System.out.println(ivySettings.getDefaultCache().getAbsolutePath());//C:\Users\Lenovo\.m2\repository\cache
        
     // create a pattern matcher
        ivySettings.addMatcher(new GlobPatternMatcher());
     // create the dependency resolvers 加载资源
        ChainResolver chainResolver = createRepoResolvers(repositories,ivySettings);
        ivySettings.addResolver(chainResolver);
        ivySettings.setDefaultResolver(chainResolver.getName());
        
        Ivy ivy = Ivy.newInstance(ivySettings);
        ResolveOptions resolveOptions = new ResolveOptions();
        resolveOptions.setTransitive(true);
        
        RetrieveOptions retrieveOptions = new RetrieveOptions();
        resolveOptions.setDownload(true);
        
        DefaultModuleDescriptor md = DefaultModuleDescriptor.newDefaultInstance(
        	    ModuleRevisionId.newInstance("org.apache.spark", "spark-submit-parent", "1.0"));
        ModuleRevisionId mdId = md.getModuleRevisionId();
        /**
:: loading settings :: url = jar:file:/C:/Users/Lenovo/.m2/repository/org/apache/ivy/ivy/2.4.0/ivy-2.4.0.jar!/org/apache/ivy/core/settings/ivysettings.xml
org.apache.spark#spark-submit-parent;1.0
         */
        System.out.println(mdId);
        System.out.println(mdId.getOrganisation());//org.apache.spark 组织
        System.out.println(mdId.getName()); //spark-submit-parent name
        
        String fileName = mdId.getOrganisation() + "-" + mdId.getName() + "-ivyConfName.xml";
        File previousResolution = new File(ivySettings.getDefaultCache(),fileName);
        if (previousResolution.exists()) previousResolution.delete();
        
        md.setDefaultConf(ivyConfName);
        addDependenciesToIvy(md);

        try {
			ResolveReport rr = ivy.resolve(md,resolveOptions);
			
			 ivy.retrieve(rr.getModuleDescriptor().getModuleRevisionId(),
			          packagesDirectory.getAbsolutePath() + File.separator +"[organization]_[artifact]-[revision].[ext]",
			          retrieveOptions.setConfs(new String[]{ivyConfName}));
			 resolveDependencyPaths(rr.getArtifacts(), packagesDirectory);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String resolveDependencyPaths(List artifacts,File cacheDirectory){
		System.out.println("list==>"+artifacts.size());
		/*System.out.println("---");
		System.out.println(artifacts.get(0));*/
		return "";
		
	}
	//添加依赖
	public void addDependenciesToIvy(DefaultModuleDescriptor md){
	
		//添加两个jar包
		ModuleRevisionId ri = ModuleRevisionId.newInstance("redis.clients", "jedis","2.5.1");
		DefaultDependencyDescriptor dd = new DefaultDependencyDescriptor(ri,true,true);
		dd.addDependencyConfiguration(ivyConfName, ivyConfName);
		md.addDependency(dd);
		
		ri = ModuleRevisionId.newInstance("mysql", "mysql-connector-java","5.1.29");
		dd = new DefaultDependencyDescriptor(ri,true,true);
		dd.addDependencyConfiguration(ivyConfName, ivyConfName);
		md.addDependency(dd);
	}
	
	public ChainResolver createRepoResolvers(String remoteRepos,IvySettings ivySettings){
		ChainResolver chainResolver = new ChainResolver();
		chainResolver.setName("aixuebo");
		String[] repos = remoteRepos.split(",");
		int index = 0 ;
		for(String reposUrl:repos){
			IBiblioResolver iBiblioResolver = new IBiblioResolver();
			iBiblioResolver.setM2compatible(true);
			iBiblioResolver.setUsepoms(true);
			iBiblioResolver.setRoot(reposUrl);
			iBiblioResolver.setName("repo-"+index);
		    chainResolver.add(iBiblioResolver);
		}
		
		IBiblioResolver localM2 = new IBiblioResolver();
		localM2.setM2compatible(true);
		localM2.setUsepoms(true);
		localM2.setRoot(new File("C:\\Users\\Lenovo\\.m2\\repository").toURI().toString());//file:/C:/Users/Lenovo/.m2/repository/
		localM2.setName("local-m2-cache");
	    chainResolver.add(localM2);
	    
	    
	    FileSystemResolver localIvy = new FileSystemResolver();
	    File localIvyRoot = new File(ivySettings.getDefaultIvyUserDir(), "local");
	    localIvy.setLocal(true);
	    localIvy.setRepository(new FileRepository(localIvyRoot));
	    
	    String ivyPattern = "[organisation]/[module]/[revision]/[type]s/[artifact](-[classifier]).[ext]";
	    System.out.println(localIvyRoot.getAbsolutePath()+"/"+ivyPattern);
	    localIvy.addIvyPattern(localIvyRoot.getAbsolutePath()+"/"+ivyPattern);
	    localIvy.setName("local-ivy-cache");
	    chainResolver.add(localIvy);
	    
	    // the biblio resolver resolves POM declared dependencies
	    IBiblioResolver br = new IBiblioResolver();
	    br.setM2compatible(true);
	    br.setUsepoms(true);
	    br.setName("central");
	    chainResolver.add(br);
	    
	    IBiblioResolver sp = new IBiblioResolver();
	    sp.setM2compatible(true);
	    sp.setUsepoms(true);
	    localM2.setRoot("http://dl.bintray.com/spark-packages/maven");
	    sp.setName("spark-packages");
	    chainResolver.add(sp);

		return chainResolver;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		IvyMain test = new IvyMain();
		test.test1();
	}
}
