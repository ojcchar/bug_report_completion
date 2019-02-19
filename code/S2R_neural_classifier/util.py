import pyhocon
import os
import errno


def get_config(filename):
  return pyhocon.ConfigFactory.parse_file(filename)

def print_config(config):
  print(pyhocon.HOCONConverter.convert(config, "hocon"))

def mkdirs(path):
  try:
    os.makedirs(path)
  except OSError as exception:
    if exception.errno != errno.EEXIST:
      raise
  return path