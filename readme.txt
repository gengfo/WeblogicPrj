Global setup: Set up git  
git config --global user.name "Forest Geng"  
git config --global user.email gengfo@gmail.com
git clone -v git@github.com:gengfo/WeblogicPrj.git  


Next steps:  
mkdir weblogicPrj  
cd weblogicPrj  
git init  
touch README  
git add README  
git commit -m 'first commit'  
git remote add origin git@github.com:gengfo/WeblogicPrj.git  
git push -u origin master


Existing Git Repo?  
cd existing_git_repo  
git remote add origin git@github.com:gengfo/WeblogicPrj.git  
git push -u origin master

