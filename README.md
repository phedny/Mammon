Mammon Website
==============

This branch holds the [Mammon](https://github.com/phedny/Mammon "Master repo") website.

It is intended to be a [GitHub page](http://pages.github.com/ "Documentation on GitHub pages").

Development
-----------

The site is developed in the `website` branch so that GitHub does not
automatically hosts it as an GitHub page. The workflow amounts to the
following.

### Setup

1. [Fork](http://help.github.com/fork-a-repo/ "GitHub on forking") the project.
2. Clone your fork.
3. Add Master remote with the following command

    > git remote add master https://github.com/phedny/Mammon.git

### Make changes

The above steps only have to be done once. The next steps detail
making changes to your clone and issuing a pull request

4. Fetch latest changes by issuing

    > git fetch master

5. Merge any change with the commands

    > git checkout website
    > git merge master/website

6. Make changes to the website using and commit them regularly.
7. Push the committed changes to your fork

    > git push origin website

8. Send a [pull request](http://help.github.com/send-pull-requests/ "GitHub on pull requests").

### Host changes

The step below should only be executed on the 
[master repository](https://github.com/phedny/Mammon "Master repo").

9. Examine pull request for the website branch.
10. Accept the changes either by 
  [auto-merging](https://github.com/blog/843-the-merge-button "GitHub on auto-merging")
  or using the 
  [fetch and merge](http://help.github.com/send-pull-requests/#fetch_and_merge "Details of pull request")
   method.
11. Use steps 4 and 5 to get the local repository in sync with the changes
12. Merge the changes to the `gh-pages` branch.

    > git checkout gh-pages
    > git merge website

13. Push the changes to the `gh-pages` to the master

    > git push origin gh-pages

14. Await the pages build notification.
15. Enjoy the new site.

The first time this is done a `gh-pages` branch should be created
first. This can be done with the following command. 

    > git checkout -b gh-pages website
