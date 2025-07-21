if [ "$#" -ne 2 ]; then
    echo "Error: two arguments required."
    echo "Example: ./commit_version.sh \"提交说明\" v0.1.1"
    exit 1
fi

COMMIT_MSG=$1
VERSION_TAG=$2

echo "📦 开始提交：$COMMIT_MSG"
echo "🏷️  版本号标签：$VERSION_TAG"
git add .
git commit -m "$COMMIT_MSG"
git tag -a $VERSION_TAG -m "$COMMIT_MSG"

git push origin main
git push origin $VERSION_TAG

echo "提交完成并已打上标签 $VERSION_TAG"