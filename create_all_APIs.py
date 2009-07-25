import os

#os.system()

games =  os.listdir('./games_src')
oss =  os.listdir('./OS_specific_files')

#oss = ['linux_32', 'windows_32', 'windows_64']
#games = ['linux_32', 'windows_32', 'windows_64']

for game in games:
  for opsys in oss:
    os.system('echo "./create_API.sh ' + game + ' ' + opsys+'"')
    os.system('./create_API.sh ' + game + ' ' + opsys)



