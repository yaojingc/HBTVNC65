DELETE FROM pub_bcr_candiattr WHERE pk_nbcr = '0001ZZ10000000003F6H';
DELETE FROM pub_bcr_elem WHERE pk_billcodebase in ( select pk_billcodebase from pub_bcr_RuleBase where nbcrcode = 'AWSZ' );
DELETE FROM pub_bcr_RuleBase WHERE nbcrcode = 'AWSZ';
DELETE FROM pub_bcr_nbcr WHERE pk_nbcr = '0001ZZ10000000003F6H';
DELETE FROM pub_bcr_OrgRela WHERE pk_billcodebase = '0001ZZ10000000003F6I';
DELETE FROM pub_bcr_RuleBase WHERE pk_billcodebase = '0001ZZ10000000003F6I';
DELETE FROM pub_bcr_elem WHERE pk_billcodeelem = '0001ZZ10000000003F6J';
DELETE FROM pub_bcr_elem WHERE pk_billcodeelem = '0001ZZ10000000003F6K';
DELETE FROM pub_bcr_elem WHERE pk_billcodeelem = '0001ZZ10000000003F6L';
